package eisenwave.torrens.img.div;

import eisenwave.torrens.object.Rectangle4i;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DividerFast implements TextureDivider {
    
    
    @Override
    public Rectangle4i[] apply(BooleanTexture texture) {
        final int limX = texture.getWidth(), limY = texture.getHeight();
        
        List<Line>[] linesLists = mergeX(texture, limX, limY);
        
        return mergeY(linesLists, limY);
    }
    
    @SuppressWarnings("unchecked")
    private static List<Line>[] mergeX(BooleanTexture texture, int limX, int limY) {
        List<Line>[] result = new LinkedList[limY];
        
        for (int y = 0; y < limY; y++) {
            List<Line> lines = result[y] = new ArrayList();
            
            int lineOrigin = -1;
            for (int x = 0; x < limX; x++) {
                
                //there is a pixel
                if (texture.contains(x, y)) {
                    //no line has been drawn yet, so the line begins at the current pixel
                    if (lineOrigin == -1) lineOrigin = x;
                }
                
                //there is no voxel, but line has begun and can be ended
                else if (lineOrigin > -1) {
                    lines.add(new Line(lineOrigin, x - 1));
                    lineOrigin = -1;
                }
            }
            
            //finish a line if it ended at the last x-coordinate
            if (lineOrigin != -1)
                lines.add(new Line(lineOrigin, limX - 1));
        }
        
        return result;
    }
    
    private static Rectangle4i[] mergeY(List<Line>[] linesLists, int limY) {
        List<Rectangle4i> result = new ArrayList<>();
        
        //loop through every line list
        for (int minY = 0; minY < limY; minY++)
            for (Line line : linesLists[minY]) {
                //turn line into plane on current y-coordinate, then proceed to stretch it into y as far as possible
                int maxY = minY;
                
                //loop through all lines further in y-direction than the current one
                mergeLoop:
                for (int i = minY + 1; i < limY; i++) {
                    
                    Iterator<Line> iter = linesLists[i].iterator();
                    //loop through all entries on the line
                    while (iter.hasNext()) {
                        Line line2 = iter.next();
                        //early merge break since all following lines will start further in x than this one
                        if (line2.min > line.min) break mergeLoop;
                        if (line2.equals(line)) {
                            //line can be merged into current plane, thus stretch the current plane and remove the line
                            maxY = i;
                            iter.remove();
                            //continue merging on next y
                            continue mergeLoop;
                        }
                    }
                    
                    break;
                }
                
                result.add(new Rectangle4i(line.min, minY, line.max, maxY));
            }
        
        return result.toArray(new Rectangle4i[result.size()]);
    }
    
    /*
    PlaneList[] result = new PlaneList[limZ];

        //loop through all z-axis planes
        for (int z = 0; z<limZ; z++) {
            PlaneList planes = result[z] = new PlaneList();

            //loop through every line list
            for (int y = 0; y<limY; y++) for (LineList.Entry line : lines[y][z]) {
                //turn line into plane on current y-coordinate, then proceed to stretch it into y as far as possible
                PlaneList.Entry plane = new PlaneList.Entry(line.min, y, line.max, y);
                planes.add(plane);

                //loop through all lines further in y-direction than the current one
                mergeLoop: for (int i = y+1; i<limY; i++) {

                    Iterator<LineList.Entry> iter = lines[i][z].iterator();
                    //loop through all entries on the line
                    while (iter.hasNext()) {
                        LineList.Entry line2 = iter.next();
                        //early merge break since all following lines will start further in x than this one
                        if (line2.min > line.min) break mergeLoop;
                        if (line2.equals(line)) {
                            //line can be merged into current plane, thus stretch the current plane and remove the line
                            plane.ymax = i;
                            iter.remove();
                            //continue merging on next y
                            continue mergeLoop;
                        }
                    }

                    break;
                }
            }
        }

        return result;
     */
    
    private static class Line {
        public final int min, max;
        
        public Line(int min, int max) {
            this.min = min;
            this.max = max;
        }
        
        @Override
        public boolean equals(Object obj) {
            return obj instanceof Line && equals((Line) obj);
        }
        
        public boolean equals(Line line) {
            return line.min == this.min && line.max == this.max;
        }
    }
    
}
