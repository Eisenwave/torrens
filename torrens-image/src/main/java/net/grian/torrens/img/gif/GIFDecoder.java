package net.grian.torrens.util.img.gif;

import net.grian.torrens.error.FileSyntaxException;
import net.grian.torrens.util.img.Texture;
import net.grian.torrens.util.ColorMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;

public class GIFDecoder implements Closeable {
    
    private final static String
        FORMAT = "javax_imageio_gif_image_1.0",
    
        EL_GLOBAL_TABLE = "GlobalColorTable",
        EL_LOGICAL_SCREEN = "LogicalScreenDescriptor",
        EL_GCE = "GraphicControlExtension",
        EL_DESCRIPTOR = "ImageDescriptor",
    
        ATTR_BACKGROUND_INDEX = "backgroundColorIndex",
        ATTR_LOG_SCR_WIDTH = "logicalScreenWidth",
        ATTR_LOG_SCR_HEIGHT = "logicalScreenHeight",
        ATTR_POS_X = "imageLeftPosition",
        ATTR_POS_Y = "imageTopPosition",
        //ATTR_WIDTH = "imageWidth",
        //ATTR_HEIGHT = "imageHeight",
        ATTR_DISPOSAL = "disposalMethod",
        ATTR_DELAY = "delayTime";
    
    @NotNull
    private final Object source;
    
    protected ImageReader reader;
    protected GIFHeader header;
    
    protected int index = 0;
    protected GIFFrame out, frame;
    
    protected Texture previous, data;
    
    protected boolean hasNext = true;
    
    public GIFDecoder(Object source) {
        this.source = Objects.requireNonNull(source);
        this.reader = ImageIO.getImageReadersByFormatName("gif").next();
    }
    
    /**
     * Reads the next frame.
     *
     * @return the next frame
     * @throws IOException if an I/O error occurs
     */
    public GIFFrame next() throws IOException {
        if (!hasNext) throw new NoSuchElementException();
        
        if (header == null) readHeader();
        
        out = frame;
        
        frame = readFrame(index++);
        
        if (frame == null)
            hasNext = false;
        
        // first option only true when loading first frame
        return out == null? frame : out;
    }
    
    /**
     * Returns whether another frame may be loaded.
     *
     * @return whether another frame may be loaded
     */
    public boolean hasNext() {
        return hasNext;
    }
    
    /**
     * Returns the GIF header. If the header has not been read yet, an attempt of reading it from the source
     * is being made.
     *
     * @return the GIF header
     */
    @NotNull
    public GIFHeader getHeader() throws IOException {
        return header==null? readHeader() : header;
    }
    
    /**
     * <p>
     *     Returns the current GIF frame or <code>null</code> if no frame has not been read yet.
     * </p>
     * <p>
     *     The frame's image data is temporary and can be safely mutated.
     * </p>
     *
     * @return the current GIF frame
     */
    @Nullable
    public GIFFrame getCurrentFrame() {
        return frame;
    }
    
    /**
     * Returns the gif data. The returned texture is a constant, updated every time a frame is read.
     *
     * @return the current frame data
     */
    public Texture getCurrentData() {
        return data;
    }
    
    /**
     * Returns the current frame index.
     *
     * @return the current frame index
     */
    public int getCurrentIndex() {
        return index;
    }
    
    // READ
    
    @NotNull
    private GIFHeader readHeader() throws IOException {
        this.reader.setInput(ImageIO.createImageInputStream(source));
        
        this.header = deserializeHeader(reader.getStreamMetadata());
    
        /*
        try {
            writeAsDebugXML(metadata.getAsTree(metadata.getNativeMetadataFormatName()), new File("F:/Porn/debug.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    
        this.data = Texture.alloc(header.getWidth(), header.getHeight());
        //this.canvas = master.getCanvasWrapper();
        
        return this.header;
    }
    
    private static void writeAsDebugXML(Node node, File out) throws Exception {
        Transformer tr = TransformerFactory.newInstance().newTransformer();
        tr.setOutputProperty(OutputKeys.INDENT, "yes");
        tr.setOutputProperty(OutputKeys.METHOD, "xml");
        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        
        tr.transform(new DOMSource(node), new StreamResult(new FileOutputStream(out)));
    }
    
    @Nullable
    private GIFFrame readFrame(int index) throws IOException {
        BufferedImage image;
        try{
            image = reader.read(index);
        }catch (IndexOutOfBoundsException io){
            return null;
        }
    
        IIOMetadataNode root = (IIOMetadataNode) reader.getImageMetadata(index).getAsTree(FORMAT);
        IIOMetadataNode gce = (IIOMetadataNode) root.getElementsByTagName(EL_GCE).item(0);
    
        /*
        if (index < 4) try {
            writeAsDebugXML(root, new File("F:/Porn/debug_"+index+".xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    
        final int delay = Integer.valueOf( gce.getAttribute(ATTR_DELAY) );
        final GIFDisposal disposal = GIFDisposal.fromName( gce.getAttribute(ATTR_DISPOSAL) );
    
        //final int w = image.getWidth(), h = image.getHeight();
        int x = 0, y = 0;
    
        NodeList children = root.getChildNodes();
        for (int nodeIndex = 0; nodeIndex < children.getLength(); nodeIndex++) {
            Node nodeItem = children.item(nodeIndex);
            if (nodeItem.getNodeName().equals(EL_DESCRIPTOR)) {
                NamedNodeMap map = nodeItem.getAttributes();
                x = Integer.valueOf(map.getNamedItem(ATTR_POS_X).getNodeValue());
                y = Integer.valueOf(map.getNamedItem(ATTR_POS_Y).getNodeValue());
                //width = Integer.valueOf(map.getNamedItem(ATTR_WIDTH).getNodeValue());
                //height = Integer.valueOf(map.getNamedItem(ATTR_HEIGHT).getNodeValue());
            }
        }
        
        if (frame != null) {
            disposeFrame();
            
            // create backup data for when data is being disposed
            if (disposal == GIFDisposal.TO_PREVIOUS && frame.getDisposalMethod() != GIFDisposal.TO_PREVIOUS) {
                previous = data.clone();
            }
        }
        
        Texture texture = Texture.wrapOrCopy(image);
        
        data.getCanvasWrapper().drawTexture(texture, x, y);
        
        return frame = new GIFFrame(texture, x, y, delay, disposal);
    }
    
    private void disposeFrame() throws FileSyntaxException {
        switch (frame.getDisposalMethod()) {
            case NONE: return;
            
            case TO_PREVIOUS:
                if (previous == null)
                    throw new FileSyntaxException("can not restore to previous at frame "+index);
    
                this.data = previous;
                break;
                
            case TO_BACKGROUND:
                data.getCanvasWrapper().drawAll(data.getBackground());
                break;
                
            default: throw new FileSyntaxException("unsupported disposal method: "+frame.getDisposalMethod());
        }
    }
    
    // MISC
    
    @Override
    public void close() {
        reader.dispose();
        frame = null;
        data = null;
    }
    
    // UTIL
    
    @NotNull
    private static GIFHeader deserializeHeader(@NotNull IIOMetadata metadata) throws FileSyntaxException {
        IIOMetadataNode globalRoot = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());
    
        NodeList globalColorTable = globalRoot.getElementsByTagName(EL_GLOBAL_TABLE);
        if (globalColorTable == null)
            throw new FileSyntaxException("metadata missing: "+ EL_GLOBAL_TABLE);
        
        NodeList globalScreeDescriptor = globalRoot.getElementsByTagName(EL_LOGICAL_SCREEN);
        if (globalScreeDescriptor == null)
            throw new FileSyntaxException("metadata missing: "+ EL_LOGICAL_SCREEN);
        
        IIOMetadataNode descriptorNode = (IIOMetadataNode) globalScreeDescriptor.item(0);
        if (descriptorNode == null)
            throw new FileSyntaxException("metadata missing: "+ EL_LOGICAL_SCREEN);
        
        IIOMetadataNode tableNode = (IIOMetadataNode) globalColorTable.item(0);
        if (tableNode == null)
            throw new FileSyntaxException("metadata missing: "+ EL_LOGICAL_SCREEN);
    
        final int[] dims = deserializeScreenDescriptor(descriptorNode);
        final GIFColorTable gifColorTable = deserializerColorTable(tableNode);
        
        return new GIFHeader(dims[0], dims[1], gifColorTable);
    }
    
    @NotNull
    private static int[] deserializeScreenDescriptor(@NotNull IIOMetadataNode descriptor) throws FileSyntaxException {
        String
            width = descriptor.getAttribute(ATTR_LOG_SCR_WIDTH),
            height = descriptor.getAttribute(ATTR_LOG_SCR_HEIGHT);
        
        try {
            return new int[] {Integer.parseInt(width), Integer.parseInt(height)};
        } catch (NumberFormatException ex) {
            throw new FileSyntaxException(ex);
        }
    }
    
    @NotNull
    private static GIFColorTable deserializerColorTable(@NotNull IIOMetadataNode colorTable) throws FileSyntaxException {
        final int[] table = new int[colorTable.getLength()];
        
        for (IIOMetadataNode entry = (IIOMetadataNode) colorTable.getFirstChild();
             entry != null;
             entry = (IIOMetadataNode) entry.getNextSibling()) try {
            final int
                i = Integer.parseInt(entry.getAttribute("index")),
                r = Integer.parseInt(entry.getAttribute("red")),
                g = Integer.parseInt(entry.getAttribute("green")),
                b = Integer.parseInt(entry.getAttribute("blue"));
            table[i] = ColorMath.fromRGB(r, g, b);
        } catch (NumberFormatException ex) {
            throw new FileSyntaxException(ex);
        }
    
        final int backIndex = Integer.parseInt(colorTable.getAttribute(ATTR_BACKGROUND_INDEX));
        
        return new GIFColorTable(table, backIndex);
    }
    
}
