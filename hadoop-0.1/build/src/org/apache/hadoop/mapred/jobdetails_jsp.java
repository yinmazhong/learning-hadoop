package org.apache.hadoop.mapred;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.apache.hadoop.mapred.*;

public final class jobdetails_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write('\n');

  String jobid = request.getParameter("jobid");
  JobTracker tracker = JobTracker.getTracker();
  JobInProgress job = (JobInProgress) tracker.getJob(jobid);
  JobProfile profile = (job != null) ? (job.getProfile()) : null;
  JobStatus status = (job != null) ? (job.getStatus()) : null;

  TaskReport[] mapTaskReports = (job != null) ? tracker.getMapTaskReports(jobid) : null;
  TaskReport[] reduceTaskReports = (job != null) ? tracker.getReduceTaskReports(jobid) : null;

      out.write("\n\n<html>\n<title>Hadoop MapReduce Job Details</title>\n<body>\n");

  if (job == null) {
    
      out.write("\n    No job found<br>\n    ");

  } else {
    
      out.write("\n<h1>Job '");
      out.print(jobid);
      out.write("'</h1>\n\n<b>Job File:</b> ");
      out.print(profile.getJobFile());
      out.write("<br>\n<b>The job started at:</b> ");
      out.print( new Date(job.getStartTime()));
      out.write("<br>\n");

  if (status.getRunState() == JobStatus.RUNNING) {
    out.print("The job is still running.<br>\n");
  } else if (status.getRunState() == JobStatus.SUCCEEDED) {
    out.print("<b>The job completed at:</b> " + new Date(job.getFinishTime()) + "<br>\n");
  } else if (status.getRunState() == JobStatus.FAILED) {
    out.print("<b>The job failed at:</b> " + new Date(job.getFinishTime()) + "<br>\n");
  }

      out.write("\n<hr>\n\n<h2>Map Tasks</h2>\n  <center>\n  <table border=2 cellpadding=\"5\" cellspacing=\"2\">\n  <tr><td align=\"center\">Task Id</td><td>Complete</td><td>State</td><td>Errors</td></tr>\n\n  ");


    for (int i = 0; i < mapTaskReports.length; i++) {
      TaskReport report = mapTaskReports[i];

      out.print("<tr><td>" + report.getTaskId() + "</td>");
      out.print("<td>" + report.getProgress() + "</td>");
      out.print("<td>" + report.getState() + "</td>");

      String[] diagnostics = report.getDiagnostics();
      for (int j = 0; j < diagnostics.length ; j++) {
        out.print("<td>" + diagnostics[j] + "</td>");
      }
      out.print("</tr>\n");
    }
  
      out.write("\n  </table>\n  </center>\n<hr>\n\n\n<h2>Reduce Tasks</h2>\n  <center>\n  <table border=2 cellpadding=\"5\" cellspacing=\"2\">\n  <tr><td align=\"center\">Task Id</td><td>Complete</td><td>State</td><td>Errors</td></tr>\n\n  ");

    for (int i = 0; i < reduceTaskReports.length; i++) {
      TaskReport report = reduceTaskReports[i];

      out.print("<tr><td>" + report.getTaskId() + "</td>");
      out.print("<td>" + report.getProgress() + "</td>");
      out.print("<td>" + report.getState() + "</td>");

      String[] diagnostics = report.getDiagnostics();
      for (int j = 0; j < diagnostics.length ; j++) {
        out.print("<td>" + diagnostics[j] + "</td>");
      }
      out.print("</tr>\n");
    }
  
      out.write("\n  </table>\n  </center>\n  ");

  }

      out.write("\n\n<hr>\n<a href=\"/jobtracker.jsp\">Go back to JobTracker</a><br>\n<a href=\"http://lucene.apache.org/hadoop\">Hadoop</a>, 2006.<br>\n</body>\n</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
