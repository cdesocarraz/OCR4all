package de.uniwue.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import de.uniwue.config.ProjectConfiguration;

/**
 * Helper class for generic controller
 */
public class GenericHelper {
    /**
     * Object to access project configuration
     */
    private ProjectConfiguration projConf;

    /**
     * Constructor
     *
     * @param projectDir Path to the project directory
     */
    public GenericHelper(String projectDir) {
        projConf = new ProjectConfiguration(projectDir);
    }

    /**
     * Constructor
     *
     * @param projConf Project configuration object
     */
    public GenericHelper(ProjectConfiguration projConf) {
        this.projConf = projConf;
    }

    /**
     * Gets all page IDs of the project
     *
     * @param imageType Type of the images
     * @return Array of page IDs
     * @throws IOException
     */
    public ArrayList<String> getPageList(String imageType) throws IOException {
        ArrayList<String> pageList = new ArrayList<String>();
        if (!new File(projConf.getImageDirectoryByType(imageType)).exists())
            return pageList;

        String imageExtension = projConf.getImageExtensionByType(imageType);
        
        // File depth of 1 -> no recursive (file)listing 
        Files.walk(Paths.get(projConf.getImageDirectoryByType(imageType)), 1)
        .map(Path::toFile)
        .filter(fileEntry -> fileEntry.isFile())
        .filter(fileEntry -> fileEntry.getName().endsWith(imageExtension))
        .sorted()
        .forEach(
            fileEntry -> { pageList.add(fileEntry.getName().replace(imageExtension, "")); }
        );
        return pageList;
    }

    /**
     * Checks if the directory of given image type exits
     *
     * @param imageType Type of the image directory (original, gray, binary, despeckled, OCR)
     * @return Information if the directory exists
     */
    public boolean checkIfImageDirectoryExists(String imageType) {
        String imageDir = projConf.getImageDirectoryByType(imageType);
        if (new File(imageDir).exists())
            return true;
        return false;
    }

    /**
     * Gets the the number of logical thread of the system
     *
     * @return Number of logical threads
     */
    public static int getLogicalThreadCount() {
        return Runtime.getRuntime().availableProcessors();
    }
}
