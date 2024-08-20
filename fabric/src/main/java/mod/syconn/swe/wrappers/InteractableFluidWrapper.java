package mod.syconn.swe.wrappers;

import mod.syconn.swe.extra.core.InteractionalFluidHandler;

public class InteractableFluidWrapper extends BlockFluidWrapper {

    private final InteractionalFluidHandler interactionalHandler;

    public InteractableFluidWrapper(InteractionalFluidHandler handler) {
        super(handler);
        this.interactionalHandler = handler;
    }

    public InteractionalFluidHandler getHandler() {
        return this.interactionalHandler;
    }
}
