package net.agilepartner.workshops.cqrs.domain.handlers;

import net.agilepartner.workshops.cqrs.core.CommandHandler;
import net.agilepartner.workshops.cqrs.core.Repository;
import net.agilepartner.workshops.cqrs.domain.InventoryItem;
import net.agilepartner.workshops.cqrs.domain.cmd.DeactivateInventoryItem;

public class DeactivateInventoryItemHandler implements CommandHandler<DeactivateInventoryItem> {
    private Repository<InventoryItem> repository;

    public DeactivateInventoryItemHandler(Repository<InventoryItem> repository) {
        this.repository = repository;
    } 

    @Override
    public void handle(DeactivateInventoryItem command) {
        InventoryItem item = repository.getById(command.aggregateId);
        item.deactivate();;
        repository.save(item);
    }
}