import CredentialsProvider from "next-auth/providers/credentials";
import { NextAuthConfig } from "next-auth";
import { authenticateUser, refreshAccessToken } from "@/lib/api/auth";

export default {
  providers: [
    CredentialsProvider({
      credentials: {
        username: { label: "Username", type: "text" },
        password: { label: "Password", type: "password" },
      },
      async authorize(credentials) {
        try {
          if (!credentials.username || !credentials.password) {
            return null;
          }

          const ret = await authenticateUser(
            credentials.username as string,
            credentials.password as string
          );

          if (!ret.success) {
            return null;
          }

          return {
            ...ret.user,
            expiresAt: ret.expiresAt,
            accessToken: ret.token,
            refreshToken: ret.refreshToken,
          };
        } catch (error) {
          return null;
        }
      },
    }),
  ],
  callbacks: {
    async jwt({ token, user }) {
      // Initial sign in
      if (user) {
        const { accessToken, expiresAt, refreshToken, ...restUser } = user;

        return {
          accessToken: accessToken,
          accessTokenExpires: (expiresAt ? expiresAt : 0) * 1000,
          refreshToken: refreshToken,
          user: restUser,
        };
      }

      console.log("Date.now()", Date.now());
      console.log("accessTokenExpires", token.accessTokenExpires);

      if (token.accessTokenExpires && Date.now() < token.accessTokenExpires) {
        return token;
      }

      return await refreshAccessToken(token);
    },
    async session({ session, token }) {
      // JWTトークンからセッションに値を渡す
      session.user.name = token.user.name;
      session.user.accessToken = token.accessToken;

      return session;
    },
  },
} satisfies NextAuthConfig;
