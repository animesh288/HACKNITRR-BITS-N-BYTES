package com.android.m2crm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class TagResolver {

    HashSet<String > discount=new HashSet<String >(Arrays.asList(new String[]{"offer", "discount", "coupon", "free"}));
    HashSet<String > settlement=new HashSet<String>(Arrays.asList(new String[]{"settlement", "payout", "payment", "income", "revenue"}));
    HashSet<String > order=new HashSet<String>(Arrays.asList(new String[]{"order", "placed", "buy", "purchase"}));
    HashSet<String > tracking=new HashSet<String>(Arrays.asList(new String[]{"tracking", "location", "reach", "track", "destination", "city"}));
    HashSet<String > refund=new HashSet<String >(Arrays.asList(new String[]{"defective", "not working", "damaged", "replace", "refund", "faulty", "fault", "broken"}));
    HashSet<String > general=new HashSet<String >(Arrays.asList(new String[]{"how to", "benefit", "use", "advantage"}));
    HashSet<String > service=new HashSet<String >(Arrays.asList(new String[]{"warranty", "guarantee", "repair", "service"}));
    HashSet<String > internal=new HashSet<String >(Arrays.asList(new String[]{"connecting call with Internal Team", "transferring"}));
    HashSet<String > callback=new HashSet<String >(Arrays.asList(new String[]{"callback", "inform"}));
    HashSet<String> technical=new HashSet<String >(Arrays.asList(new String[]{"website", "technical", "bug", "server"}));
    HashSet<String> logistics=new HashSet<String >(Arrays.asList(new String[]{"delay", "delivered", "dispatch", "delivery", "cod", "supply chain"}));
    HashSet<String> complaint=new HashSet<String>(Arrays.asList(new String[]{"complaint", "report", "raise", "ticket"}));

    public ArrayList<String> getTags(String convo){
        ArrayList<String > tags=new ArrayList<>();

        convo=convo.toLowerCase();

        for(String x:discount) if(convo.contains(x)){
            tags.add("Discount Requirement");
            break;
        }for(String x:settlement) if(convo.contains(x)){
            tags.add("Settlement & Payout");
            break;
        }for(String x:order) if(convo.contains(x)){
            tags.add("Order Query");
            break;
        }for(String x:tracking) if(convo.contains(x)){
            tags.add("Tracking");
            break;
        }for(String x:refund) if(convo.contains(x)){
            tags.add("Replacement / Refund");
            break;
        }for(String x:general) if(convo.contains(x)){
            tags.add("General info");
            break;
        }for(String x:service) if(convo.contains(x)){
            tags.add("Service Related");
            break;
        }for(String x:internal) if(convo.contains(x)){
            tags.add("Internal Team Required");
            break;
        }for(String x:callback) if(convo.contains(x)){
            tags.add("Callback Assisted");
            break;
        }for(String x:logistics) if(convo.contains(x)){
            tags.add("Logistic issue");
            break;
        }for(String x:technical) if(convo.contains(x)){
            tags.add("Technical issue");
            break;
        }for(String x:complaint) if(convo.contains(x)){
            tags.add("Complaint");
            break;
        }

        return tags;
    }

}
