import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 *
 * @author Enrique Paez
 */
public abstract class Animal implements Actor {

    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's position.
    private Location location;
    // The field occupied.
    private Field field;
    // The animal's age.
    private int age;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a new animal. An animal may be created with age
     * zero (a newborn) or with a random age.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location) {
        alive = true;
        this.field = field;
        setLocation(location);
        age = 0;
    }

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    public Field getField() {
        return field;
    }

    /**
     * Return the animal's age.
     * @return The animal's age.
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the animal´s age.
     * @param age The new animal´s age.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Check whether the actor is active or not.
     * @return true if the actor is still active.
     */
    public boolean isActive() {
        return alive;
    }

    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Actor> newAnimals);

    /**
     * @return the animal´s breeding age.
     */
    abstract protected int getBreedingAge();

    /**
     * @return the animal´s maximum age.
     */
    abstract protected int getMaxAge();

    /**
     * @return the animal´s breeding probability.
     */
    abstract protected double getBreedingProb();

    /**
     * @return the animal´s maximum litter size.
     */
    abstract protected int getMaxLitterSize();

    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation) {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * An animal can breed if it has reached the breeding age.
     * @return true if the animal can breed
     */
    public boolean canBreed() {
        return getAge() >= getBreedingAge();
    }

    /**
     * Increase the age. This could result in the fox's death.
     */
    public void incrementAge() {
        setAge(getAge() + 1);

        if(getAge() > getMaxAge()) {
            setDead();
        }
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    public int breed() {
        int births = 0;

        if(canBreed() && rand.nextDouble() <= getBreedingProb()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }

    /**
     * Check whether or not this animal is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newAnimals A list to return newly born animals.
     */
    public void giveBirth(List<Actor> newAnimals) {
        // New animals are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();

        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Animal youngAnimal;

            if(this instanceof Rabbit) {
                youngAnimal = new Rabbit(false, field, loc);
            } else if (this instanceof Fox) {
                youngAnimal = new Fox(false, field, loc);
            } else {
                youngAnimal = new Fox(false, field, loc);
            }
            newAnimals.add(youngAnimal);
        }
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead() {
        alive = false;

        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }
}
