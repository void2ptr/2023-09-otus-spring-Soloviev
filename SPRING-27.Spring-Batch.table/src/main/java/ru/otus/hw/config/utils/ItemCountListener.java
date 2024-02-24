package ru.otus.hw.config.utils;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class ItemCountListener implements ChunkListener {

    @Override
    public void beforeChunk(ChunkContext context) {
    }

    @Override
    public void afterChunk(ChunkContext context) {
        long count = context.getStepContext().getStepExecution().getReadCount();
        System.out.println("ItemCount: " + count);
    }

    @Override
    public void afterChunkError(ChunkContext context) {
    }
}
