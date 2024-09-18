"use server";

import { auth, signOut } from "@/auth";
import { revokeAccessToken } from "@/lib/api/auth";
import { LOGIN_ROUTE } from "@/routes";

type logoutSuccess = {
  success: true;
};

type logoutFailure = {
  success: false;
  message: string;
};

type logoutResult = logoutSuccess | logoutFailure;

export const logout = async (): Promise<logoutResult> => {
  const session = await auth();

  // リフレッシュトークンを廃棄
  await revokeAccessToken(session?.user?.accessToken as String);

  // NextAuthのsignOut関数を使用してログアウト
  await signOut({ redirectTo: LOGIN_ROUTE });

  return {
    success: true,
  };
};
