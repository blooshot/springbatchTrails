package com.krishnadev.InventoryManagement.product.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class TextItemProcessor  implements ItemProcessor<String, String> {

    @Override
    public String process(String item) throws Exception {
        String masked = item.replaceAll("\\d","*");
        return masked;
    }
}
