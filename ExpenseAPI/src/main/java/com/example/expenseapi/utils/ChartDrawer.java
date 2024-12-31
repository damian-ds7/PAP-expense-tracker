package com.example.expenseapi.utils;

import com.example.expenseapi.service.ExpenseService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
public class ChartDrawer {
    private final ExpenseService expenseService;
    public ChartDrawer(ExpenseService expenseService) {this.expenseService = expenseService;}

    private byte[] convertImageToByteArray(File imageFile) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", baos);
        return baos.toByteArray();
    }

    public byte[] barChart(String year) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Double> monthlyExpenses = expenseService.getMonthlyExpensesForGroup(year, "PLN");
        for (Map.Entry<String, Double> entry : monthlyExpenses.entrySet()) {
            dataset.addValue(entry.getValue(), "Expenses", entry.getKey());
        }
        JFreeChart barChart = ChartFactory.createBarChart(
                String.format("Monthly expenses of %s", year),
                "Month",
                "Total",
                dataset);
        File chartFile = new File("temp.png");
        ChartUtils.saveChartAsPNG(chartFile, barChart, 800, 600);
        byte[] coded = convertImageToByteArray(chartFile);
        chartFile.delete();
        return coded;

    }
}
