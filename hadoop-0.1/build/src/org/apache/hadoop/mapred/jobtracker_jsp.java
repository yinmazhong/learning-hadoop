package org.apache.hadoop.mapred;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import org.apache.hadoop.mapred.*;

public final class jobtracker_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


  JobTracker tracker = JobTracker.getTracker();
  String trackerLabel = tracker.getJobTrackerMachine() + ":" + tracker.getTrackerPort();
  private static DecimalFormat percentFormat = new DecimalFormat("##0.00");

  public void generateTaskTrackerTable(JspWriter out) throws IOException {
    Collection c = tracker.taskTrackers();

    if (c.size() == 0) {
      out.print("There are currently no known TaskTracker(s).");
    } else {
      out.print("<center>\n");
      out.print("<table border=\"2\" cellpadding=\"5\" cellspacing=\"2\">\n");
      out.print("<tr><td align=\"center\" colspan=\"4\"><b>Task Trackers</b></td></tr>\n");
      out.print("<tr><td><b>Name</b></td><td><b>Host</b></td><td><b># running tasks</b></td><td><b>Secs since heartbeat</b></td></tr>\n");

      for (Iterator it = c.iterator(); it.hasNext(); ) {
        TaskTrackerStatus tt = (TaskTrackerStatus) it.next();
        long sinceHeartbeat = System.currentTimeMillis() - tt.getLastSeen();
        if (sinceHeartbeat > 0) {
          sinceHeartbeat = sinceHeartbeat / 1000;
        }
        int numCurTasks = 0;
        for (Iterator it2 = tt.taskReports(); it2.hasNext(); ) {
          it2.next();
          numCurTasks++;
        }

        out.print("<tr><td>" + tt.getTrackerName() + "</td><td>" + tt.getHost() + "</td><td>" + numCurTasks + "</td><td>" + sinceHeartbeat + "</td></tr>\n");
      }
      out.print("</table>\n");
      out.print("</center>\n");
    }
  }

  public void generateJobTable(JspWriter out, String label, Vector jobs) throws IOException {
      out.print("<center>\n");
      out.print("<table border=\"2\" cellpadding=\"5\" cellspacing=\"2\">\n");
      out.print("<tr><td align=\"center\" colspan=\"8\"><b>" + label + " Jobs </b></td></tr>\n");
      if (jobs.size() > 0) {
        out.print("<tr><td><b>Jobid</b></td><td><b>User</b></td>");
        out.print("<td><b>Name</b></td>");
        out.print("<td><b>% complete</b></td><td><b>Required maps</b></td>");
        out.print("<td><b>maps completed</b></td>");
        out.print("<td><b>Required reduces</b></td>");
        out.print("<td><b>reduces completed</b></td></tr>\n");
        for (Iterator it = jobs.iterator(); it.hasNext(); ) {
          JobInProgress job = (JobInProgress) it.next();
          JobProfile profile = job.getProfile();
          JobStatus status = job.getStatus();
          String jobid = profile.getJobId();
          double completedRatio = (0.5 * (100 * status.mapProgress())) +
                                 (0.5 * (100 * status.reduceProgress()));

          int desiredMaps = job.desiredMaps();
          int desiredReduces = job.desiredReduces();
          int completedMaps = job.finishedMaps();
          int completedReduces = job.finishedReduces();
          String name = profile.getJobName();

          out.print("<tr><td><a href=\"jobdetails.jsp?jobid=" + jobid + "\">" + 
                    jobid + "</a></td><td>"+ profile.getUser() + "</td><td>" +
                    ("".equals(name) ? "&nbsp;" : name) + "</td><td>" +
                    percentFormat.format(completedRatio) + "%</td><td>" + 
                    desiredMaps + "</td><td>" + completedMaps + "</td><td>" + 
                    desiredReduces + "</td><td> " + completedReduces + 
                    "</td></tr>\n");
        }
      } else {
        out.print("<tr><td align=\"center\" colspan=\"8\"><i>none</i></td></tr>\n");
      }
      out.print("</table>\n");
      out.print("</center>\n");
  }

  public void generateSummaryTable(JspWriter out) throws IOException {
    ClusterStatus status = tracker.getClusterStatus();
    out.print("<table border=\"2\" cellpadding=\"5\" cellspacing=\"2\">\n"+
              "<tr><th>Maps</th><th>Reduces</th>" + 
              "<th>Capacity</th><th>Nodes</th></tr>\n");
    out.print("<tr><td>" + status.getMapTasks() + "</td><td>" +
              status.getReduceTasks() + "</td><td>" + 
              status.getMaxTasks() + "</td><td>" +
              status.getTaskTrackers() + "</td></tr></table>\n");
  }

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
      out.write("\n\n<html>\n\n<title>Hadoop MapReduce General Administration</title>\n\n<body>\n<h1>JobTracker '");
      out.print(trackerLabel);
      out.write("'</h1>\n\nThis JobTracker has been up since ");
      out.print( new Date(tracker.getStartTime()));
      out.write(".<br>\n<hr>\n<h2>Cluster Summary</h2>\n<center>\n");
 
   generateSummaryTable(out); 

      out.write("\n</center>\n<hr>\n\n\n<h2>Task Trackers</h2>\n");

  generateTaskTrackerTable(out);

      out.write("\n\n<hr>\n<h2>Running Jobs</h2>\n");

    generateJobTable(out, "Running", tracker.runningJobs());

      out.write("\n<hr>\n\n<h2>Completed Jobs</h2>\n");

    generateJobTable(out, "Completed", tracker.completedJobs());

      out.write("\n<hr>\n\n<h2>Failed Jobs</h2>\n");

    generateJobTable(out, "Failed", tracker.failedJobs());

      out.write("\n<hr>\n<a href=\"http://lucene.apache.org/hadoop\">Hadoop</a>, 2006.<br>\n</body>\n</html>\n");
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
