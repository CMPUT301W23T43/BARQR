package com.example.barqrxmls;

/***
 * Simple singleton container class for the CurrentUser, so that any class which wants to act upon
 * or reference the current user can just get this instance and work with it.
 */
public class CurrentUser {
    private static CurrentUser instance = null;
    private User user;

    /***
     * Empty constructor used privately
     */
    private CurrentUser() {
    }

    /***
     * Get the instance of the CurrentUser class
     * @return instance
     */
    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }

        return instance;
    }

    /***
     * Sets the CurrentUser's user field.
     * @param user The user to become the CurrentUser
     */
    public void setUser(User user) {
        this.user = user;
    }

    /***
     * Get the CurrentUser's User object
     * @return User -- the current user object
     */
    public User getUser() {
        return user;
    }
}
