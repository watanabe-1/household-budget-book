/**
 * The default redirect path after logging in
 */
export const DEFAULT_LOGIN_REDIRECT = "/";

/**
 * The login path
 */
export const LOGIN_ROUTE = "/auth/login";

/**
 * An array of routes that are used for authentication
 * These routes will redirect logged in users to DEFAULT_LOGIN_REDIRECT
 */
export const authRoutes = [LOGIN_ROUTE];

/**
 * The prefix for API authentication routes
 * Routes that start with this prefix are used for API authentication purposes
 */
export const API_AUTH_PREFIX = "/api/auth";
