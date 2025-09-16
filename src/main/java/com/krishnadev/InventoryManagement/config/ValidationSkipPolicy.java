package com.krishnadev.InventoryManagement.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import com.krishnadev.InventoryManagement.config.ValidationException;

import java.util.List;

@Slf4j
public class ValidationSkipPolicy implements SkipPolicy {

    private final ItemWriter<ErrorRecordDTO> errorWriter;

    public ValidationSkipPolicy(ItemWriter<ErrorRecordDTO> errorWriter) {
        this.errorWriter = errorWriter;
    }

    @Override
    public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
        log.debug("ShouldSkip: {}", skipCount);
        if (t instanceof ValidationException) {
            ValidationException ex = (ValidationException) t;
            try {
                errorWriter.write(Chunk.of(new ErrorRecordDTO(ex.getLine(), ex.getReason())));
            } catch (Exception e) {
                // Handle the case where the error writer fails
                throw new SkipLimitExceededException(skipCount, e );
            }
            return true; // Skip this record
        }
        return false; // Do not skip other exceptions
    }
}
