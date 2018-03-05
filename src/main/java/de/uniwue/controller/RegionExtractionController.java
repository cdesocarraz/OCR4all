package de.uniwue.controller;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import de.uniwue.helper.RegionExtractionHelper;

/**
 * Controller class for pages of region extraction module
 * Use response.setStatus to trigger AJAX fail (and therefore show errors)
 */
@Controller
public class RegionExtractionController {
    /**
     * Response to the request to send the content of the /RegionExtraction page
     *
     * @param session Session of the user
     * @return Returns the content of the /RegionExtraction page
     */
    @RequestMapping("/RegionExtraction")
    public ModelAndView show(HttpSession session) {
        ModelAndView mv = new ModelAndView("regionExtraction");

        String projectDir = (String)session.getAttribute("projectDir");
        if (projectDir == null) {
            mv.addObject("error", "Session expired.\nPlease return to the Project Overview page.");
            return mv;
        }

        return mv;
    }

    /**
     * Response to the request to execute the region extraction of the specified images
     * 
     * @param pageIds Identifiers of the chosen pages (e.g 0002,0003)
     * @param session Session of the user
     * @param response Response to the request
     */
    @RequestMapping(value = "/ajax/regionExtraction/execute", method = RequestMethod.POST)
    public @ResponseBody void execute(
                @RequestParam("pageIds[]") String[] pageIds,
                @RequestParam("spacing") int spacing,
                @RequestParam("usespacing") boolean usespacing,
                @RequestParam("avgbackground") boolean avgbackground,
                HttpSession session, HttpServletResponse response
            ) {
        String projectDir = (String) session.getAttribute("projectDir");
        if (projectDir == null || projectDir.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        // Keep a single helper object in session
        RegionExtractionHelper regionExtractionHelper = (RegionExtractionHelper) session.getAttribute("regionExtractionHelper");
        if (regionExtractionHelper == null) {
            regionExtractionHelper = new RegionExtractionHelper(projectDir);
            session.setAttribute("regionExtractionHelper", regionExtractionHelper);
        }

        if (regionExtractionHelper.isRegionExtractionRunning() == true) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        try {
            regionExtractionHelper.executeRegionExtraction(Arrays.asList(pageIds), spacing, usespacing, avgbackground);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            regionExtractionHelper.resetProgress();
        }
    }

    /**
     * Response to the request to return the progress status of the region extraction service
     *
     * @param session Session of the user
     * @return Current progress (range: 0 - 100)
     */
    @RequestMapping(value = "/ajax/regionExtraction/progress" , method = RequestMethod.GET)
    public @ResponseBody int progress(HttpSession session) {
        RegionExtractionHelper regionExtractionHelper = (RegionExtractionHelper) session.getAttribute("regionExtractionHelper");
        if (regionExtractionHelper == null)
            return -1;

        return regionExtractionHelper.getProgress();
    }

    /**
     * Response to the request to cancel the regionExtraction process
     *
     * @param session Session of the user
     * @param response Response to the request
     */
    @RequestMapping(value = "/ajax/regionExtraction/cancel", method = RequestMethod.POST)
    public @ResponseBody void cancel(HttpSession session, HttpServletResponse response) {
        RegionExtractionHelper regionExtractionHelper = (RegionExtractionHelper) session.getAttribute("regionExtractionHelper");
        if (regionExtractionHelper == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        regionExtractionHelper.cancelProcess();
    }
}
