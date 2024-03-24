import React, { ReactElement } from "react";
import {
  Button,
  Dialog,
  DialogHeader,
  DialogBody,
  DialogFooter,
} from "@material-tailwind/react";
import { size } from "@material-tailwind/react/types/components/dialog";

type propTypes = {
  open: boolean;
  onClose: () => void;
  size?: size;
  header: string;
  // content?: string | undefined;
  children?: ReactElement;
};

const Modal: React.FC<propTypes> = (prop) => {
  return (
    <Dialog
      size={prop.size ? prop.size : "xs"}
      open={prop.open}
      handler={prop.onClose}
      placeholder={""}
    >
      <DialogHeader placeholder={""}>{prop.header}</DialogHeader>
      <DialogBody placeholder={""}>
        {/* {prop.content} */}
        {prop.children}
      </DialogBody>
      <DialogFooter placeholder={""}>
        <Button
          variant="gradient"
          color="green"
          onClick={prop.onClose}
          placeholder={""}
        >
          <span>OK</span>
        </Button>
      </DialogFooter>
    </Dialog>
  );
};

export default Modal;
