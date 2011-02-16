package com.xebia.devradar;

import java.util.List;

/**
 * Dto returned by a rest resources with tree filter :
 * hudson/user/<usernmae>/api/json/?tree=property[address]
 *
 * Json Example : {"property":[{"address":"mail@host.com"},null,{}]} 
 */
public class HudsonUserDetailDTO {

    /**
     * List of user properties
     */
    public List<HudsonUserPropertyDTO> property;

    public String getUserMail() {
        if (property != null && property.size() > 0) {
            return property.get(0).address;
        }
        return null;
    }

    /**
     * One of these property can have an mail address.
     * Used to determine the default user mail.
     */
    static private class HudsonUserPropertyDTO {
        public String address;
    }
}
