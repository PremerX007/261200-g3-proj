import React from "react";
import {
  Button,
  Dialog,
  DialogHeader,
  DialogBody,
  DialogFooter,
} from "@material-tailwind/react";

type propTypes = {
  open: boolean;
  onClose: () => void;
  header: string;
  content: string | undefined;
};

const Modal: React.FC<propTypes> = ({ open, onClose, header, content }) => {
  return (
    <Dialog size={"xs"} open={open} handler={onClose} placeholder={""}>
      <DialogHeader placeholder={""}>{header}</DialogHeader>
      <DialogBody placeholder={""}>{content}</DialogBody>
      <DialogFooter placeholder={""}>
        <Button
          variant="gradient"
          color="green"
          onClick={onClose}
          placeholder={""}
        >
          <span>OK</span>
        </Button>
      </DialogFooter>
    </Dialog>
  );
};

export default Modal;
