"use server";

import { JWT } from "next-auth/jwt";

// APIのベースURL
const API_URL = process.env.NEXT_PUBLIC_API_URL;

type User = {
  name: string;
  role: string;
};

type Token = {
  token: string;
  refreshToken: string;
  expiresAt: number;
  user: User;
};

type AuthenticationSuccess = {
  success: true;
} & Token;

type AuthenticationFailure = {
  success: false;
  message: string;
};

type AuthenticationResult = AuthenticationSuccess | AuthenticationFailure;

export const authenticateUser = async (
  username: string,
  password: string
): Promise<AuthenticationResult> => {
  // ユーザー名とパスワードをBase64エンコード
  const credentials = btoa(`${username}:${password}`);

  try {
    const response = await fetch(`${API_URL}/oauth2/token`, {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        Authorization: `Basic ${credentials}`,
      },
    });

    if (!response.ok) {
      return {
        success: false,
        message: "Authentication failed",
      };
    }

    const tokenJson = (await response.json()) as Token;

    console.log(tokenJson);

    return {
      success: true,
      ...tokenJson,
    };
  } catch (error) {
    // ネットワークエラーなどの処理
    return {
      success: false,
      message: error instanceof Error ? error.message : "Unknown error",
    };
  }
};

export const refreshAccessToken = async (token: JWT) => {
  try {
    const response = await fetch(`${API_URL}/oauth2/refresh`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token.refreshToken}`,
      },
    });

    if (!response.ok) {
      return {
        ...token,
        error: "RefreshAccessTokenError",
      };
    }

    const tokenJson = (await response.json()) as Token;

    console.log(tokenJson);

    return {
      ...token,
      accessToken: tokenJson.token,
      accessTokenExpires: tokenJson.expiresAt * 1000,
    };
  } catch (error) {
    console.log(error);
    return {
      ...token,
      error: "RefreshAccessTokenError",
    };
  }
};

export const revokeAccessToken = async (accessToken: String) => {
  try {
    const response = await fetch(`${API_URL}/oauth2/revoke`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });

    if (!response.ok) {
      return {
        success: false,
        error: "RevokeRefreshTokenError",
      };
    }

    console.log(await response.text);

    return {
      success: true,
    };
  } catch (error) {
    console.log(error);
    return {
      success: false,
      error: "RevokeRefreshTokenError",
    };
  }
};
