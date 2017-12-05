// CRCracker32
// Created by Michael Krickl in 2017

import java.nio.ByteBuffer;
import java.util.*;
import java.util.zip.CRC32;

public class CRCracker32 {

  private static List<Integer> checksums = new ArrayList<>();

  private static int found = 0;
  private static Map<Integer, Integer> solutions = new HashMap<>();

  public static void main(String[] args) {

    System.out.println();

    try {

      parseArgs(args);
    }
    catch(Exception e) {

      printHelp();
      return;
    }

    System.out.println("Starting CRCracker");

    crack();

    System.out.println();
    System.out.println("Finished:");
    for(Map.Entry<Integer, Integer> e : solutions.entrySet()) {

      int crc32 = e.getKey();
      int solution = e.getValue();

      System.out.println(Integer.toHexString(crc32) + " -> " + Integer.toHexString(solution));
    }
  }

  private static void parseArgs(String[] args)
      throws Exception {

    for(String s : args) {

      s = s.replace("0x", "").replace("0X", "");
      checksums.add(Integer.parseUnsignedInt(s, 16));
    }

    if(checksums.size() == 0) throw new Exception();


    System.out.println("Input:");
    for(int i : checksums) {

      System.out.println("  " + Integer.toHexString(i));
    }

    System.out.println();
  }

  private static void printHelp() {

    System.out.println("CRCracker32");
    System.out.println("A simple CRC32 Brute-Forcer by m1ckey");
    System.out.println();
    System.out.println("Usage:");
    System.out.println("  java CRCracker32 <crc32 in hex>...");
    System.out.println();
    System.out.println("Example:");
    System.out.println("  java CRCracker32 0x13371337");
    System.out.println("  java CRCracker32 0x1337");
    System.out.println("  java CRCracker32 1337");
  }

  private static void crack() {

    for(long l = 0; l < Math.pow(2, 32); l++) {

      CRC32 crc32 = new CRC32();

      int i = (int) l;
      crc32.update(ByteBuffer.allocate(4).putInt(i).array());
      int checksum = (int) crc32.getValue();

      if(checksums.contains(checksum)) {

        System.out.println(
            "Found: " + Integer.toHexString(checksum) + " -> " + Integer.toHexString(i));
        solutions.put(checksum, i);

        found++;

        if(found == checksums.size()) {
          return;
        }
      }


      if(l % (long) (Math.pow(2, 32) / 100) == 0) {
        System.out.printf("%3d%%%n", (int) (l / Math.pow(2, 32) * 100));
      }
    }
  }
}