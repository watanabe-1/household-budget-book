import { auth } from "@/auth";
import {
  API_AUTH_PREFIX,
  DEFAULT_LOGIN_REDIRECT,
  LOGIN_ROUTE,
  authRoutes,
} from "./routes";

export default auth((req) => {
  const { nextUrl } = req;
  const isLoggedIn = !!req.auth;

  const isApiAuthRoute = nextUrl.pathname.startsWith(API_AUTH_PREFIX);
  const isAuthRoute = authRoutes.includes(nextUrl.pathname);

  if (isApiAuthRoute) {
    return;
  }

  if (isAuthRoute) {
    if (isLoggedIn) {
      return Response.redirect(new URL(DEFAULT_LOGIN_REDIRECT, nextUrl));
    }
    return;
  }

  if (!isLoggedIn) {
    return Response.redirect(new URL(LOGIN_ROUTE, nextUrl));
  }

  return;
});

export const config = {
  /**
   * 下記に設定した値にマッチするパスは対象から除外される
   * あとでパスの内容を考えること
   */
  matcher: ["/((?!api|_next/static|_next/image|favicon.ico).*)"],
};
