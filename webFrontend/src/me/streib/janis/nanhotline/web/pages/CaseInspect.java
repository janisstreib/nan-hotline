package me.streib.janis.nanhotline.web.pages;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.streib.janis.nanhotline.web.dbobjects.Action;
import me.streib.janis.nanhotline.web.dbobjects.Case;
import me.streib.janis.nanhotline.web.dbobjects.Status;
import me.streib.janis.nanhotline.web.dbobjects.Supporter;

import org.cacert.gigi.output.template.IterableDataset;

public class CaseInspect extends Page {

    public CaseInspect() {
        super("Case");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp,
            Map<String, Object> vars, Matcher match) throws IOException,
            SQLException {
        String pathInfo = req.getPathInfo();
        int caseId = Integer.parseInt(pathInfo.substring(
                pathInfo.lastIndexOf('/') + 1, pathInfo.length()));
        Case c = Case.getCaseById(caseId);
        if (c == null) {
            resp.sendError(404);
            return;
        }
        vars.put("case_id", c.getId());
        vars.put("open", c.getStatus() == Status.OPEN);
        vars.put("case_tite", c.getTitle());
        Supporter supp = c.getAssignedSupporter();
        if (supp != null) {
            vars.put("case_supporter", supp.getName());
        }
        vars.put("description", c.getDescription());
        final LinkedList<Action> actions = Action.getByCase(c);
        vars.put("actions", new IterableDataset() {

            @Override
            public boolean next(Map<String, Object> vars) {
                if (actions.isEmpty()) {
                    return false;
                }
                Action a = actions.removeFirst();
                vars.put("action", a.output(vars));
                return true;
            }
        });
        getDefaultTemplate().output(resp.getWriter(), vars);
    }

    @Override
    public boolean needsLogin() {
        return true;
    }

    @Override
    public boolean needsTemplate() {
        return true;
    }

}
