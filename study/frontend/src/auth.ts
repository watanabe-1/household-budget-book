import NextAuth from "next-auth";
import authConfig from "@/auth.config";

const SECRET = process.env.AUTH_SECRET;

export const {
  handlers: { GET, POST },
  auth,
  signIn,
  signOut,
} = NextAuth({
  secret: SECRET,
  ...authConfig,
});
