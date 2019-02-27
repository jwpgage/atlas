package org.openstreetmap.atlas.geography.boundary;

import java.io.IOException;
import java.util.List;

import org.openstreetmap.atlas.geography.MultiPolygon;
import org.openstreetmap.atlas.geography.Polygon;
import org.openstreetmap.atlas.geography.Rectangle;
import org.openstreetmap.atlas.geography.clipping.Clip;
import org.openstreetmap.atlas.geography.clipping.Clip.ClipType;
import org.openstreetmap.atlas.streaming.resource.File;
import org.openstreetmap.atlas.streaming.resource.LineWriter;

public class CountryBoundaryMapWithOcean
{

    public static void main(final String[] args)
    {
        final File boundaryFile = new File(
                "/Users/jamesgage/Desktop/osm_full_world/osm_world_boundary.txt");
        final CountryBoundaryMap boundaryMap = CountryBoundaryMap.fromPlainText(boundaryFile);
        final Polygon shardPoly = Polygon.wkt(
                "POLYGON ((-90 16.6361919, -90 21.9430455, -84.375 21.9430455, -84.375 16.6361919, -90 16.6361919))");
        final Rectangle shardInitialBounds = shardPoly.bounds();
        final File outputFile = new File("/Users/jamesgage/Desktop/oceanBoundary/testOSM.txt");
        final LineWriter writer = new LineWriter(outputFile);
        MultiPolygon shardMulti = MultiPolygon.forPolygon(shardPoly);
        writer.writeLine(shardMulti.toWkt());
        int i = 1;
        final List<CountryBoundary> boundaries = boundaryMap.boundaries(shardInitialBounds);
        for (final CountryBoundary boundary : boundaries)
        {
            final Clip clip = shardMulti.clip(boundary.getBoundary(), ClipType.NOT);
            shardMulti = clip.getClipMultiPolygon();
            System.out.println(i + "/" + boundaries.size());
            i++;
            writer.writeLine(shardMulti.toWkt());

        }
        try
        {
            writer.close();
        }
        catch (final IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // org.locationtech.jtslab.SnapOverlayFunctions.intersection(null, null, 0);

    }

}
