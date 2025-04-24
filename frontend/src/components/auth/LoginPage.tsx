import React, { useState } from 'react';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

const formSchema = z.object({
  email: z.string().email({ message: "유효한 이메일을 입력해주세요." }),
  password: z.string().min(3, { message: "비밀번호는 최소 3자 이상이어야 합니다." }),
});

interface LoginPageProps {
  onLogin: () => void;
}

const LoginPage: React.FC<LoginPageProps> = ({ onLogin }) => {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  async function onSubmit(values: z.infer<typeof formSchema>) {
    setIsLoading(true);
    setError("");

    try {
      const response = await fetch("http://localhost:8000/login", {
        method: "POST",
        credentials: "include", // 세션 쿠키 포함
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(values),
      });

      if (!response.ok) {
        const err = await response.json();
        throw new Error(err.detail || "로그인 실패");
      }

      const data = await response.json();
      console.log("로그인 성공", data);
      localStorage.setItem("user", JSON.stringify({ email: data.email }));
      onLogin(); // 상위 컴포넌트 콜백

    } catch (err: any) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <div className="p-6 bg-white rounded-lg shadow-md w-full max-w-md mx-auto">
      <div className="flex justify-center mb-6">
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none"
             xmlns="http://www.w3.org/2000/svg">
          <path d="M12 2C12 2 9 5 9 7C9 8.5 10 9.5 12 9.5C14 9.5 15 8.5 15 7C15 5 12 2 12 2Z"
                fill="#000000" />
          <path d="M12 10C8.13401 10 5 13.134 5 17C5 20.866 8.13401 22 12 22C15.866 22 19 20.866 19 17C19 13.134 15.866 10 12 10Z"
                fill="#000000" />
          <path d="M9 14C9 14 9 16 9 17M12 13C12 13 12 16 12 18M15 14C15 14 15 16 15 17"
                stroke="#FFFFFF" strokeWidth="1" strokeLinecap="round" />
          <path d="M8 15H16M7 17H17M8 19H16"
                stroke="#FFFFFF" strokeWidth="1" strokeLinecap="round" />
        </svg>
      </div>

      <h1 className="text-2xl font-bold text-center mb-6">Fineapple 로그인</h1>

      {error && (
        <div className="text-red-600 text-sm mb-4 text-center">
          {error}
        </div>
      )}

      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
          <FormField
            control={form.control}
            name="email"
            render={({ field }) => (
              <FormItem>
                <FormLabel>이메일</FormLabel>
                <FormControl>
                  <Input placeholder="name@example.com" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="password"
            render={({ field }) => (
              <FormItem>
                <FormLabel>비밀번호</FormLabel>
                <FormControl>
                  <Input type="password" placeholder="••••••" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <Button type="submit" className="w-full mt-6" disabled={isLoading}>
            {isLoading ? "로그인 중..." : "로그인"}
          </Button>
        </form>
      </Form>

      <div className="mt-4 text-center text-sm text-gray-500">
        <p>
          계정이 없으신가요?{" "}
          <a href="#" className="text-black font-medium">가입하기</a>
        </p>
      </div>
    </div>
  );
};

export default LoginPage;
