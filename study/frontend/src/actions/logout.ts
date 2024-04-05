"use server";

import { auth, signOut } from "@/auth";
import { revokeAccessToken } from "@/lib/api/auth";

type logoutSuccess = {
  success: true;
};

type logoutFailure = {
  success: false;
  message: string;
};

type logoutResult = logoutSuccess | logoutFailure;

export const logout = async (): Promise<logoutResult> => {
  try {
    const session = await auth();

    // リフレッシュトークンを廃棄
    await revokeAccessToken(session?.user?.accessToken as String);

    // NextAuthのsignOut関数を使用してログアウト
    await signOut();

    return {
      success: true,
    };
  } catch (error) {
    throw error;
  }
};
