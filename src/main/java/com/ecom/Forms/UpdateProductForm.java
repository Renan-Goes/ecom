package com.ecom.Forms;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateProductForm {

    Map<String, String> values = new HashMap<String, String>();
    
}
