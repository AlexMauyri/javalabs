package ru.ssau.tk.DoubleA.javalabs.ui;

import org.springframework.stereotype.Component;
import ru.ssau.tk.DoubleA.javalabs.functions.MathFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.io.FunctionsIO;

import java.io.*;

@Component
public class FunctionSerializer {
    public byte[] serializeCustomFunction(MathFunction function) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(function);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize function", e);
        }
    }

    public byte[] serializeByte(TabulatedFunction function) {
        byte[] serializedFunction = null;
        try(ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            BufferedOutputStream outputStream = new BufferedOutputStream(byteOutputStream)) {
            FunctionsIO.serialize(outputStream, function);
            serializedFunction = byteOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return serializedFunction;
    }

    public String serializeJson(TabulatedFunction function) {
        String serializedFunction = null;
        try(StringWriter stringWriter = new StringWriter();
            BufferedWriter bufferedWriter = new BufferedWriter(stringWriter)) {
            FunctionsIO.serializeJson(bufferedWriter, function);
            serializedFunction = stringWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return serializedFunction;
    }

    public String serializeXml(TabulatedFunction function) {
        String serializedFunction = null;
        try(StringWriter stringWriter = new StringWriter();
            BufferedWriter bufferedWriter = new BufferedWriter(stringWriter)) {
            FunctionsIO.serializeXml(bufferedWriter, function);
            serializedFunction = stringWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return serializedFunction;
    }

    public MathFunction deserializeFunction(byte[] data) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return (MathFunction) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to deserialize function", e);
        }
    }
}
