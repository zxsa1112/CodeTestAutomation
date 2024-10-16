import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class ChartCreator {
    public static void main(String[] args) {
        // 데이터셋 생성
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1, "Rule", "java/unused-parameter");
        // ... (다른 데이터 추가)

        // 차트 생성
        JFreeChart chart = ChartFactory.createBarChart(
            "CodeQL Analysis Results",
            "Rule",
            "Count",
            dataset
        );

        // 차트를 표시할 패널 생성
        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
}