import java.rmi.Naming;

public class ClienteRMI {

  private static final int N = ClaseRMI.N;
  private static final int M = ClaseRMI.M;
  
  static double[][] separa_matriz(double[][] A, int inicio) {
    double[][] M = new double[N / 2][N];
    for (int i = 0; i < N / 2; i++)
      for (int j = 0; j < N; j++)
        M[i][j] = A[i + inicio][j];
    return M;
  }

  static void acomoda_matriz(double[][] C, double[][] A, int renglon, int columna) {
    for (int i = 0; i < N / 2; i++)
      for (int j = 0; j < N / 2; j++)
        C[i + renglon][j + columna] = A[i][j];
  }

  static void printMatrix(double matrix[][], int rows, int cols) {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        System.out.printf("%8.1f", matrix[i][j]);
      }
      System.out.println("");
    }
  }

  public static void main(String args[]) throws Exception {
    double[][] A = new double[N][M];
    double[][] B = new double[M][N];
    double[][] C = new double[N][N];
    double checksum = 0;

    // Inicializar las matrices A, B y C
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < M; j++) {
        A[i][j] = 3 * i + 2 * j;
        B[i][j] = 2 * i - 3 * j;
        C[i][j] = 0;
      }
    }

    System.out.println("Matriz A:");
    printMatrix(A, N, N);
    System.out.println("Matriz B:");
    printMatrix(B, N, N);

    // transpone la matriz B, la matriz traspuesta queda en B
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < i; j++) {
        double x = B[i][j];
        B[i][j] = B[j][i];
        B[j][i] = x;
      }
    }

    System.out.println("Matriz B^T:");
    printMatrix(B, N, N);

    // Separar la matriz en matrices más pequeñas
    double[][] A1 = separa_matriz(A, 0);
    double[][] A2 = separa_matriz(A, N / 2);
    double[][] B1 = separa_matriz(B, 0);
    double[][] B2 = separa_matriz(B, N / 2);

    // obtiene una referencia que "apunta" al objeto remoto asociado a la URL
    InterfaceRMI nodo1 = (InterfaceRMI) Naming.lookup("rmi://10.5.0.4/multiplicamatrix");
    InterfaceRMI nodo2 = (InterfaceRMI) Naming.lookup("rmi://10.6.0.4/multiplicamatrix");

    // Multiplica las matrices
    double[][] C1 = nodo1.multiplica_matrices(A1, B1);
    double[][] C2 = nodo1.multiplica_matrices(A1, B2);
    double[][] C3 = nodo2.multiplica_matrices(A2, B1);
    double[][] C4 = nodo2.multiplica_matrices(A2, B2);

    // Une la matriz resultante
    acomoda_matriz(C, C1, 0, 0);
    acomoda_matriz(C, C2, 0, N / 2);
    acomoda_matriz(C, C3, N / 2, 0);
    acomoda_matriz(C, C4, N / 2, N / 2);

    System.out.println("Matriz C:");
    printMatrix(C, N, N);

    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        checksum += C[i][j];
      }
    }
    System.out.println("Checksum de C: " + checksum);
  }
}
