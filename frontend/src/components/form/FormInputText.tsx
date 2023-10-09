import { Control, Controller, FieldValues } from "react-hook-form";
import TextField from "@mui/material/TextField";

type Props = {
  name: string;
  control: Control<FieldValues> | undefined;
  label: string;
  value: string;
  type?: string;
};

export default function FormInputText({
  name,
  control,
  label,
  value,
  type,
}: Props) {
  return (
    <Controller
      name={name}
      control={control}
      render={({ field: { onChange, value }, fieldState: { error } }) => (
        <TextField
          helperText={error ? error.message : null}
          size="small"
          error={!!error}
          onChange={onChange}
          value={value}
          fullWidth
          label={label}
          variant="outlined"
          type={type ?? "text"}
        />
      )}
      defaultValue={value}
    />
  );
}
