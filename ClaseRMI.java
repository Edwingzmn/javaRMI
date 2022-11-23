import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class ClaseRMI extends UnicastRemoteObject implements InterfaceRMI {
  public static final int N = 6;
  public static final int M = 5;
  
  // es necesario que el contructor default de la clase ClaseRMI invoque el constructor de la super-clase
  public ClaseRMI() throws RemoteException {
    super();
  }

  public double[][] multiplica_matrices(double[][] A, double[][] B) throws RemoteException {
    double[][] C = new double[N / 2][N / 2];
    for (int i = 0; i < N / 2; i++)
        for (int j = 0; j < N / 2; j++)
            for (int k = 0; k < N; k++)
                C[i][j] += A[i][k] * B[j][k];
    return C;
  }
}