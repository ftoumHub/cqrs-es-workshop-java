package net.agilepartner.workshops.cqrs.core.infrastructure;

import net.agilepartner.workshops.cqrs.core.Command;
import net.agilepartner.workshops.cqrs.core.CommandHandler;
import net.agilepartner.workshops.cqrs.core.DomainException;
import net.agilepartner.workshops.cqrs.core.infrastructure.memory.InMemoryCommandResolver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SimpleCommandDispatcherTests {

    private CommandResolver resolver;
    private Boolean handlerCalled;

    public class MyCommand extends Command {
        public MyCommand() {
        }
    }

    public class MyCommandHandler implements CommandHandler<MyCommand> {
        @Override
        public void handle(MyCommand command) throws DomainException {
            handlerCalled = true;
        }
    }

    @Before
    public void setup(){
        resolver = InMemoryCommandResolver.getInstance();
        resolver.register(new MyCommandHandler(), MyCommand.class);
    }

    @Test
    public void findHandlerForMyCommand() throws DomainException {

        CommandHandler<MyCommand> handler = spy(resolver.findHandlerFor(MyCommand.class));
        assertNotNull(handler);

        MyCommand cmd = new MyCommand();
        handler.handle(cmd);

        verify(handler, times(1)).handle(cmd);
    }

    @Test
    public void dispatchMyCommand() throws DomainException {
        handlerCalled = false;

        SimpleCommandDispatcher dispatcher = new SimpleCommandDispatcher(resolver);

        dispatcher.dispatch(new MyCommand());
        assertTrue(handlerCalled);
    }

}