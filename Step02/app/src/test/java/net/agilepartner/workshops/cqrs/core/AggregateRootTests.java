package net.agilepartner.workshops.cqrs.core;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import io.vavr.collection.List;
import org.junit.Test;

public class AggregateRootTests {

	@Test
	public void createAggregate() {
		UUID id = UUID.randomUUID();
		String name = "DDD rocks!";
		MyAggregate aggregate = new MyAggregate(id, name);

		assertEquals(id, aggregate.id);
		List<NameChanged> events = getEvents(aggregate);
		assertEquals(1, events.size());

		NameChanged evt = events.last();
		assertEquals(id, evt.aggregateId);
		assertEquals(1, evt.version);
		assertEquals(name, evt.name);
	}

	@Test
	public void changeName(){
		UUID id = UUID.randomUUID();
		String name = "CQRS/ES rocks even more!";
		MyAggregate aggregate = new MyAggregate(id, "DDD rocks!");

		aggregate.changeName(name);

		List<NameChanged> events = getEvents(aggregate);
		assertEquals(2, events.size());
		NameChanged evt = events.last();
		assertEquals(id, evt.aggregateId);
		assertEquals(2, evt.version);
		assertEquals(name, evt.name);
	}

	@Test
	public void loadFromHistory() {
		UUID id = UUID.randomUUID();
		String name1 = "DDD rocks!";
		String name2 = "CQRS/ES rocks even more!";

		List<NameChanged> history = List.empty();
		NameChanged evt1 = new NameChanged(id, name1);
		evt1.version = 1;
		NameChanged evt2 = new NameChanged(id, name2);
		evt2.version = 2;

		history = history.append(evt1);
		history = history.append(evt2);

		//Act
		MyAggregate aggregate = new MyAggregate(id);
		aggregate.loadFromHistory(history);

		//Assert
		List<NameChanged> events = getEvents(aggregate);
		assertEquals(events.size(), 0);
		assertEquals(2, aggregate.version);
		assertEquals(name2, aggregate.getName());
	}

	private List<NameChanged> getEvents(AggregateRoot root)
	{
		List<NameChanged> events = List.empty();
		for (Event evt : root.getUncommittedChanges()) {
			if (evt instanceof NameChanged)
				events = events.append((NameChanged)evt);
		}
		return events;
	}

}