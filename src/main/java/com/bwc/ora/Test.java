package com.bwc.ora;

import com.bwc.ora.io.TiffReader;
import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Brandon on 5/30/2017.
 */
public class Test {

    static HashMap<Integer, String> map = new HashMap<>();

    static {
        map.put(5, "TYPE_3BYTE_BGR");
        map.put(6, "TYPE_4BYTE_ABGR");
        map.put(7, "TYPE_4BYTE_ABGR_PRE");
        map.put(12, "TYPE_BYTE_BINARY");
        map.put(10, "TYPE_BYTE_GRAY");
        map.put(13, "TYPE_BYTE_INDEXED");
        map.put(0, "TYPE_CUSTOM");
        map.put(2, "TYPE_INT_ARGB");
        map.put(3, "TYPE_INT_ARGB_PRE");
        map.put(4, "TYPE_INT_BGR");
        map.put(1, "TYPE_INT_RGB");
        map.put(9, "TYPE_USHORT_555_RGB");
        map.put(8, "TYPE_USHORT_565_RGB");
        map.put(11, "TYPE_USHORT_GRAY");
    }

    public static void main(String[] args) throws IOException {

        // Use the filters to construct the walker
        FooDirectoryWalker walker = new FooDirectoryWalker(HiddenFileFilter.VISIBLE,
                FileFilterUtils.suffixFileFilter(".tif"));

        List<File> tifFiles = walker.run();

        tifFiles.forEach(tif -> {
            try {
                BufferedImage bi = TiffReader.readTiffImage(tif);
                System.out.println("Type: " + map.get(bi.getType()) + ", File: " + tif.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private static class FooDirectoryWalker extends DirectoryWalker<File> {
        public FooDirectoryWalker(IOFileFilter dirFilter, IOFileFilter fileFilter) {
            super(dirFilter, fileFilter, -1);
        }

        List<File> run() throws IOException {
            LinkedList<File> ret = new LinkedList<>();
            walk(new File("D:\\Documents\\ContractWork\\Carrol Lab\\LRP Analysis App\\Example Human OCTs"), ret);
            return ret;
        }

        @Override
        protected void handleFile(File file, int depth, Collection<File> results) throws IOException {
            results.add(file);
        }
    }

}
