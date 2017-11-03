package net.grian.torrens.util.img.gif;

import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

public class GIFEncoder implements Closeable {
    
    private final Object target;
    private ImageWriter writer;
    
    private ImageWriteParam imageWriteParam;
    private IIOMetadata imageMetaData;
    private IIOMetadataNode root;
    private String metaFormatName;
    
    public GIFEncoder(Object target) {
        this.target = Objects.requireNonNull(target);
    }
    
    public void writeHeader(GIFHeader header) throws IOException {
        init();
    
        setLoop(header.isLoop());
        
        writer.prepareWriteSequence(null);
    }
    
    public void write(GIFFrame frame) throws IOException {
        int delay = frame.getDelayMillis() / 10;
    
        IIOMetadataNode gce = new IIOMetadataNode("GraphicControlExtension");
        gce.setAttribute("disposalMethod", frame.getDisposalMethod().toString());
        gce.setAttribute("userInputFlag", frame.hasUserInput()? "TRUE" : "FALSE");
        gce.setAttribute("transparentColorFlag", frame.hasTransparency()? "TRUE" : "FALSE");
        gce.setAttribute("delayTime", Integer.toString(delay));
        gce.setAttribute("transparentColorIndex", "0");
    
        root.appendChild(gce);
        imageMetaData.setFromTree(metaFormatName, root);
        
        IIOImage iioImage = new IIOImage(frame.getData().getImageWrapper(), null, imageMetaData);
        writer.writeToSequence(iioImage, imageWriteParam);
    }
    
    private void init() throws IOException {
        ImageOutputStream imgStream = ImageIO.createImageOutputStream(target);
        this.writer = getWriter();
        this.writer.setOutput(imgStream);
        
        ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_ARGB);
        
        imageWriteParam = writer.getDefaultWriteParam();
        
        imageMetaData = writer.getDefaultImageMetadata(imageTypeSpecifier, imageWriteParam);
        
        metaFormatName = imageMetaData.getNativeMetadataFormatName();
        
        root = (IIOMetadataNode) imageMetaData.getAsTree(metaFormatName);
        
        // IIOMetadataNode commentsNode = getNode(root, "CommentExtensions");
        // commentsNode.setAttribute("CommentExtension", "Created by MAH");
    }
    
    /**
     * Enables or disables constant looping of the gif animation.
     *
     * @param loop whether the animation should loop
     */
    private void setLoop(boolean loop) {
        IIOMetadataNode appNode = getNode(root, "ApplicationExtensions");
        IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");
        child.setAttribute("applicationID", "NETSCAPE");
        child.setAttribute("authenticationCode", "2.0");
        
        int loopBit = loop? 0 : 1;
        
        child.setUserObject(new byte[] {0x1, (byte) (loopBit & 0xFF), (byte) ((loopBit >> 8) & 0xFF)});
        
        appNode.appendChild(child);
    }
    
    // UTIL
    
    /**
     * Returns the first available GIF ImageWriter using
     * ImageIO.getImageWritersBySuffix("gif").
     *
     * @return a GIF ImageWriter object
     * @throws IIOException if no GIF image writers are returned
     */
    private static ImageWriter getWriter() throws IIOException {
        Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix("gif");
        if (!iter.hasNext()) {
            throw new IIOException("No GIF Image Writers Exist");
        } else {
            return iter.next();
        }
    }
    
    /**
     * Returns an existing child node, or creates and returns a new child node (if the requested node does not exist).
     *
     * @param rootNode the <tt>IIOMetadataNode</tt> to search for the child node.
     * @param nodeName the name of the child node.
     *
     * @return the child node, if found or a new node created with the given name.
     */
    private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
        final int length = rootNode.getLength();
        for (int i = 0; i < length; i++)
            if (rootNode.item(i).getNodeName().equalsIgnoreCase(nodeName))
                return ((IIOMetadataNode) rootNode.item(i));
        
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return(node);
    }
    
    @Override
    public void close() throws IOException {
        writer.endWriteSequence();
        writer.dispose();
    }
    
}
