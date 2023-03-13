package com.chuxu.BackTrackingAlgorithmWithReduction.part01_preHandleData.RealCase;

import com.chuxu.entity.Facility;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class RealCase {
    public static int dimension = 21;
    public static final double[] ci =
            {191, 185, 218, 177, 256, 206, 246, 282, 184, 287, 312, 319, 342, 201, 311, 279, 336, 325, 295, 316, 198};
    public static final double[] pi =
            {179.7977, 42.2829, 144.3031, 115.0556, 205.8063, 63.9789, 223.6246, 170.4626, 42.2829, 179.7977, 161.1808, 190.1887, 212.3107, 167.9425, 216.9694, 232.4273, 252.4639, 225.843, 205.8063, 180.6496, 174.6421};
    public static final LinkedHashSet<Facility> V = new LinkedHashSet<>();
    public static double[][] disMatrix = new double[dimension][dimension];
    public static final double φc = 0.2;
    public static final double φp = 0.2;  //本例中都取0.2是会得到性质7疯狂降阶的情况，然后通过性质4直接得到了最优解
    public static double P;
    public static double C;

    public static void main(String[] args) throws Exception {
        preHandle();
    }

    public static void preHandle() throws Exception {
        capsulateDisMatrix();
        for (double[] matrix : disMatrix) {
            System.out.println(Arrays.toString(matrix));
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        P = Arrays.stream(pi).reduce(0.0, Double::sum) * φp;
        C = Arrays.stream(ci).reduce(0.0, Double::sum) * φc;
        P = Double.parseDouble(decimalFormat.format(P));
        C = Double.parseDouble(decimalFormat.format(C));
        System.out.println("P = " + P);
        System.out.println("C = " + C);
        for (int i = 0; i < pi.length; i++) {
            V.add(new Facility(i + 1, ci[i], pi[i], pi[i] / ci[i]));
        }
    }

    //0.1-导入数据集并读取距离矩阵
    public static void capsulateDisMatrix() throws Exception {
        String path = "E:\\Java\\IDEA_Project\\Papers\\Paper03\\src\\main\\resources\\";
        //1.创建一个工作簿，使用excel能操作的，它都能操作
        //(1)获取文件流
        FileInputStream fileInputStream = new FileInputStream(path + "上海真实案例素材.xlsx");
        //(2)把这个流放到这个工作簿里
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        //2.获取一个工作表sheet，1代表第二个sheet，获取距离矩阵
        Sheet sheet = workbook.getSheetAt(1);
        //循环读取表中的数据
        int rowCount = sheet.getPhysicalNumberOfRows();  //获取总行数
        for (int i = 0; i < rowCount; i++) {
            Row curRow = sheet.getRow(i);  //获取当前行
            int colCount = curRow.getPhysicalNumberOfCells();  //获取当前行的列数
            for (int j = i + 1; j < colCount; j++) {
                Cell curCell = curRow.getCell(j);  //获取当前单元格
                if (curCell != null) {
                    disMatrix[i][j] = disMatrix[j][i] = curCell.getNumericCellValue();
                }
            }
        }
    }
}
