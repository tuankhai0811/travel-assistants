package com.tuankhai.travelassistants.webservice.request;

import com.tuankhai.travelassistants.webservice.main.BasicRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tuank on 22/10/2017.
 */

public class GetAllScheduleRequest extends BasicRequest {
        public final String ROUTES = "Schedule/get/email";

        private String email;

        public GetAllScheduleRequest(String email) {
            this.email = email;
        }

        @Override
        public Map<String, Object> params() {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("email", email);
            return params;
        }

        @Override
        public String[] path() {
            return ROUTES.split("/");
        }
}
