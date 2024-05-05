import java.util.List;

/**
 * An interface representing shared characteristics for simulation´s actor.
 *
 * @author Enrique Paez
 */
public interface Actor {
    /**
     * Make this actor act - that is: make it do
     * whatever it wants/needs to do.
     * @param newActors A list to receive newly active actors.
     */
    void act(List<Actor> newActors);

    /**
     * Check whether the actor is active or not.
     * @return true if the actor is still active.
     */
    boolean isActive();
}
