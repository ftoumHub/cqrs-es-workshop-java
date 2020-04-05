package net.agilepartner.workshops.cqrs.domain.handlers;

import net.agilepartner.workshops.cqrs.core.CommandHandler;
import net.agilepartner.workshops.cqrs.core.Repository;
import net.agilepartner.workshops.cqrs.domain.InventoryItem;
import net.agilepartner.workshops.cqrs.domain.InventoryItemDeactivatedException;
import net.agilepartner.workshops.cqrs.domain.cmd.RenameInventoryItem;

public class RenameInventoryItemHandler implements CommandHandler<RenameInventoryItem> {
    private Repository<InventoryItem> repository;

    public RenameInventoryItemHandler(Repository<InventoryItem> repository) {
        this.repository = repository;
    } 

    @Override
    public void handle(RenameInventoryItem command) throws InventoryItemDeactivatedException {
        InventoryItem item = repository.getById(command.aggregateId);
        item.rename(command.name);
        repository.save(item);
    }
}