package com.gogiigames.unityxcodeschemegen;

import com.dd.plist.*;
import java.io.*;

public class Main {

    public static void main(String[] args) throws Exception {

        /*
        String projFolder = null;

        String prevArg = "";
        for(String s : args) {
            if(prevArg.equals("-schema")) {
                generateSchema(s);
            }
            prevArg = s;
        }*/
    }

    public static void moveArchive(String destination) {

    }

    public static void generateSchema(String projFolder) {
        String projFileName = "Unity-iPhone.xcodeproj";
        String projPath = projFolder + "/" + projFileName;

        String targetId = null;
        String targetName = null;
        String productName = null;

        NSDictionary plist = (NSDictionary) PropertyListParser.parse(new File(projPath + "/project.pbxproj"));
        NSDictionary objects = (NSDictionary)plist.get("objects");

        String rootObjectId = (String)plist.get("rootObject").toString();
        NSDictionary rootObject = (NSDictionary)objects.get(rootObjectId);

        NSArray targets = (NSArray)rootObject.get("targets");

        targetId = targets.lastObject().toString();
        NSDictionary target = (NSDictionary)objects.get(targetId);
        targetName = target.get("name").toString();

        String configListId = target.get("buildConfigurationList").toString();
        NSDictionary configList = (NSDictionary)objects.get(configListId);

        NSArray configs = (NSArray)configList.get("buildConfigurations");
        for(int i = 0; i < configs.count(); ++i) {
            String configId = configs.objectAtIndex(i).toString();
            NSDictionary config = (NSDictionary)objects.get(configId);

            if(config.get("name").toString().equals("Release")) {
                NSDictionary buildSettings = (NSDictionary)config.get("buildSettings");
                productName = buildSettings.get("PRODUCT_NAME").toString();
                break;
            }
        }

        if(targetId == null || targetName == null || productName == null) {
            throw new Exception("Failed to read xcode project");
        }

        System.out.println("Target ID: " + targetId);
        System.out.println("Target Name: " + targetName);
        System.out.println("Product Name: " + productName);

        (new File(projPath + "/xcshareddata/xcschemes")).mkdirs();

        PrintWriter writer = new PrintWriter(projPath + "/xcshareddata/xcschemes/" + targetName + ".xcscheme", "UTF-8");
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.println("<Scheme");
        writer.println("        LastUpgradeVersion = \"0460\"");
        writer.println("        version =\"1.3\">");
        writer.println("  <BuildAction");
        writer.println("          parallelizeBuildables = \"YES\"");
        writer.println("          buildImplicitDependencies = \"YES\">");
        writer.println("    <BuildActionEntries>");
        writer.println("      <BuildActionEntry");
        writer.println("              buildForTesting = \"YES\"");
        writer.println("              buildForRunning = \"YES\"");
        writer.println("              buildForProfiling = \"YES\"");
        writer.println("              buildForArchiving = \"YES\"");
        writer.println("              buildForAnalyzing = \"YES\">");
        writer.println("        <BuildableReference");
        writer.println("                BuildableIdentifier = \"primary\"");
        writer.println("                BlueprintIdentifier = \"" + targetId + "\"");
        writer.println("                BlueprintName = \"" + targetName + "\"");
        writer.println("                BuildableName = \"" + productName + ".app\"");
        writer.println("                ReferencedContainer = \"container:" + projFileName + "\">");
        writer.println("        </BuildableReference>");
        writer.println("      </BuildActionEntry>");
        writer.println("    </BuildActionEntries>");
        writer.println("  </BuildAction>");
        writer.println("  <ArchiveAction");
        writer.println("          buildConfiguration = \"Release\"");
        writer.println("          revealArchiveInOrganizer = \"YES\">");
        writer.println("  </ArchiveAction>");
        writer.println("</Scheme>");
        writer.close();

        System.out.println("Successfully generated scheme");
    }
}
