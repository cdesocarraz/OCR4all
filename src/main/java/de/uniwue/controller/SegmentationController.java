package de.uniwue.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import de.uniwue.helper.SegmentationHelper;

/**
 * Controller class for pages of segmentation module
 * Use response.setStatus to trigger AJAX fail (and therefore show errors)
 */
@Controller
public class SegmentationController {
    /**
     * Response to the request to send the content of the /Segmentation page
     *
     * @param session Session of the user
     * @return Returns the content of the /Segmentation page
     */
    @RequestMapping("/Segmentation")
    public ModelAndView show(HttpSession session) {
        ModelAndView mv = new ModelAndView("segmentation");

        String projectDir = (String)session.getAttribute("projectDir");
        if (projectDir == null) {
            mv.addObject("error", "Session expired.\nPlease return to the Project Overview page.");
            return mv;
        }

        return mv;
    }

    /**
     * Response to the request to copy the XML files
     *
     * @param imageType Type of the images (binary,despeckled)
     * @param session Session of the user
     * @param response Response to the request
     */
    @RequestMapping(value = "/ajax/segmentation/execute", method = RequestMethod.POST)
    public @ResponseBody void execute(
               @RequestParam("imageType") String segmentationImageType,
               HttpSession session, HttpServletResponse response
               
           ) {
        String projectDir = (String) session.getAttribute("projectDir");
        if (projectDir == null || projectDir.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        // Keep a single helper object in session
        SegmentationHelper segmentationHelper = (SegmentationHelper) session.getAttribute("segmentationHelper");
        if (segmentationHelper == null) {
            segmentationHelper = new SegmentationHelper(projectDir);
            session.setAttribute("segmentationHelper", segmentationHelper);
        }

        if (segmentationHelper.isSegmentationRunning() == true) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        String projectImageType  = (String) session.getAttribute("imageType");

        try {
            segmentationHelper.MoveExtractedSegments(segmentationImageType, projectImageType);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            segmentationHelper.resetProgress();
        }
    }

    /**
     * Response to the request to return the progress status of the segmentation service
     *
     * @param session Session of the user
     * @return Current progress (range: 0 - 100)
     */
    @RequestMapping(value = "/ajax/segmentation/progress" , method = RequestMethod.GET)
    public @ResponseBody int progress(HttpSession session) {
        SegmentationHelper segmentationHelper = (SegmentationHelper) session.getAttribute("segmentationHelper");
        if (segmentationHelper == null)
            return -1;

        return segmentationHelper.getProgress();
    }

    /**
     * Response to the request to cancel the segmentation copy process
     *
     * @param session Session of the user
     * @param response Response to the request
     */
    @RequestMapping(value = "/ajax/segmentation/cancel", method = RequestMethod.POST)
    public @ResponseBody void cancel(HttpSession session, HttpServletResponse response) {
        SegmentationHelper segmentationHelper = (SegmentationHelper) session.getAttribute("segmentationHelper");
        if (segmentationHelper == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        segmentationHelper.cancelProcess();
    }
}