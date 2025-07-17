import React, { useState, useCallback, createContext, useContext } from "react";
import GenericToast from "./GenericToast.tsx";

type ToastContextType = {
    showToast: (message: string, success: boolean) => void;
};

const ToastContext = createContext<ToastContextType | undefined>(undefined);

export const useToast = () => {
    const context = useContext(ToastContext);
    if (!context) throw new Error("useToast must be used within ToastProvider");
    return context;
};

export const ToastProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [show, setShow] = useState(false);
    const [message, setMessage] = useState("");
    const [success, setSuccess] = useState(false);
    const showToast = useCallback((msg: string, success: boolean) => {
        setMessage(msg);
        setSuccess(success);
        setShow(true);
    }, []);

    return (
        <ToastContext.Provider value={{ showToast }}>
            {children}
            <GenericToast shown={show} setShow={setShow} isSuccess={success}>
                {message}
            </GenericToast>
        </ToastContext.Provider>
    );
};
