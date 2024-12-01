package ru.ssau.tk.DoubleA.javalabs.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import ru.ssau.tk.DoubleA.javalabs.functions.ArrayTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.Point;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.functions.factory.TabulatedFunctionFactory;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public final class FunctionsIO {
    private FunctionsIO() {
        throw new UnsupportedOperationException();
    }

    public static void writeTabulatedFunction(BufferedWriter writer, TabulatedFunction function) {
        PrintWriter printWriter = new PrintWriter(writer);

        printWriter.println(function.getCount());

        for (Point point : function) {
            printWriter.printf("%f %f\n", point.x, point.y);
        }

        printWriter.flush();
    }

    public static void writeTabulatedFunction(BufferedOutputStream outputStream, TabulatedFunction function) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        dataOutputStream.writeInt(function.getCount());

        for (Point point : function) {
            dataOutputStream.writeDouble(point.x);
            dataOutputStream.writeDouble(point.y);
        }

        dataOutputStream.flush();
    }

    public static TabulatedFunction readTabulatedFunction(BufferedReader reader, TabulatedFunctionFactory factory) throws IOException {
        String string = reader.readLine();
        int count = Integer.parseInt(string);

        double[] xValues = new double[count];
        double[] yValues = new double[count];

        NumberFormat numberFormat = NumberFormat.getInstance(Locale.forLanguageTag("ru"));

        for (int i = 0; i < count; ++i) {
            string = reader.readLine();
            String[] numbers = string.split(" ");
            try {
                xValues[i] = numberFormat.parse(numbers[0]).doubleValue();
                yValues[i] = numberFormat.parse(numbers[1]).doubleValue();
            } catch (ParseException e) {
                throw new IOException(e);
            }
        }

        return factory.create(xValues, yValues);
    }

    public static TabulatedFunction readTabulatedFunction(BufferedInputStream inputStream, TabulatedFunctionFactory factory) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        int elementsCount = dataInputStream.readInt();
        double[] xValues = new double[elementsCount];
        double[] yValues = new double[elementsCount];

        for (int counter = 0; counter < elementsCount; counter++) {
            xValues[counter] = dataInputStream.readDouble();
            yValues[counter] = dataInputStream.readDouble();
        }

        return factory.create(xValues, yValues);
    }

    public static void serialize(BufferedOutputStream stream, TabulatedFunction function) throws IOException {
        ObjectOutputStream objectStream = new ObjectOutputStream(stream);

        objectStream.writeObject(function);

        objectStream.flush();
    }

    public static TabulatedFunction deserialize(BufferedInputStream stream) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(stream);
        Object function = objectInputStream.readObject();
        return (TabulatedFunction)function;
    }

    public static void serializeXml(BufferedWriter writer, TabulatedFunction function) throws IOException {
        XStream xStream = new XStream();
        writer.write(xStream.toXML(function));
        writer.flush();
    }

    public static TabulatedFunction deserializeXml(BufferedReader reader) {
        XStream xStream = new XStream();
        xStream.allowTypeHierarchy(ArrayTabulatedFunction.class);
        xStream.allowTypeHierarchy(LinkedListTabulatedFunction.class);
        return (TabulatedFunction) xStream.fromXML(reader);
    }

    public static void serializeJson(BufferedWriter writer, TabulatedFunction function) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        writer.write(objectMapper.writeValueAsString(function));

        writer.flush();
    }

    public static TabulatedFunction deserializeJson(BufferedReader reader) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Object function = objectMapper.readerFor(TabulatedFunction.class).readValue(reader);
        return (TabulatedFunction) function;
    }
}