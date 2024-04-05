"use client";
import { login } from "@/actions/login";
import { useState, useTransition } from "react";

const Login: React.FC = () => {
  const [username, setUsername] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [errorMessage, setErrorMessage] = useState<string>("");
  const [isPending, startTransition] = useTransition();

  const handleLogin = async () => {
    startTransition(async () => {
      const ret = await login(username, password);

      if (ret.success) {
        // 明示的にクリアすることでログイン後はメモリに保持していないようにする
        setUsername("");
        setPassword("");
      } else {
        setErrorMessage(ret.message);
      }
    });
  };

  return (
    <div className="container text-center">
      <div className="row row justify-content-center">
        <div className="col-md-5">
          <div className="login-form bg-light mt-4 p-4">
            <main className="form-signin">
              <form action={handleLogin}>
                <h1 className="h3 mb-3 fw-normal">Spring学習 ログイン</h1>
                <div className="form-floating">
                  <input
                    type="text"
                    id="username"
                    name="username"
                    className="form-control"
                    placeholder="ユーザID"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    disabled={isPending}
                    required
                  />
                  <label htmlFor="username">ユーザID</label>
                </div>
                <div className="form-floating">
                  <input
                    type="password"
                    id="password"
                    name="password"
                    className="form-control"
                    placeholder="パスワード"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    disabled={isPending}
                    required
                  />
                  <label htmlFor="password">パスワード</label>
                </div>
                <button className="w-100 btn btn-lg btn-primary" type="submit">
                  ログイン
                </button>
                {errorMessage && <p>{errorMessage}</p>}
              </form>
              <hr className="mt-4" />
            </main>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
