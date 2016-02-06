package me.streib.janis.nanhotline.web.dbobjects;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.cacert.gigi.output.template.Outputable;

public class WizardCall extends Action {
    private String supporteeSIPURI;
    private Phone supporterPhone;
    private String path;

    public WizardCall(ResultSet res) throws SQLException {
        super(res);
        this.supporteeSIPURI = res.getString("supportee_sip_uri");
        this.path = res.getString("path");
        this.supporterPhone = Phone.getById(res.getInt("supporter_phone"));
    }

    @Override
    public Outputable output(PrintWriter out, Map<String, Object> vars) {
        return getDefaultTemplate();
    }

}
