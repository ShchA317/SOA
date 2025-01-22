package soa.lab4.organization.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter extends XmlAdapter<String, Date> {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    @Override
    public Date unmarshal(String v) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.parse(v);
    }

    @Override
    public String marshal(Date v) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(v);
    }
}
