package utils;

/******************************************************************************
 *  Compilation:  javac Matrix.java
 *  Execution:    java Matrix
 *
 *  A bare-bones immutable data type for M-by-N matrices.
 *
 ******************************************************************************/

//				UTILISATION
//		double[][] d = { { 1, 2, 3 }, { 4, 5, 6 }, { 9, 1, 3} };
//        Matrix D = new Matrix(d);
//        D.show();        
//        System.out.println("Creation\n");
//
//        Matrix A = Matrix.random(5, 5);
//        A.show(); 
//        System.out.println("Random\n");
//
//        A.swap(1, 2);
//        A.show(); 
//        System.out.println("swap\n");
//
//        Matrix B = A.transpose();
//        B.show(); 
//        System.out.println("traspose\n");
//
//        Matrix C = Matrix.identity(5);
//        C.show(); 
//        System.out.println("identity\n");
//
//        A.plus(B).show();
//        System.out.println("plus\n");
//
//        B.times(A).show();
//        System.out.println("*\n");
//
//        // shouldn't be equal since AB != BA in general    
//        System.out.println("A*B = B*A ? : " + A.times(B).eq(B.times(A)));
//        System.out.println();
//
//        Matrix b = Matrix.random(5, 1);
//        b.show();
//        System.out.println("random\n");
//
//        Matrix x = A.solve(b);
//        x.show();
//        System.out.println("solve\n");
//
//        A.times(x).show();

public class Matrix
{
	static private double diversite = 1;
	private final int M;             // number of rows
	private final int N;             // number of columns
	private final double[][] data;   // M-by-N array
	    
	/********************* BEGINING OF PERSOLANISED SECTION *********************/
	    
	public int[] getSize()
	{
		int[] tmp = {M,N};
		return tmp;
	}
	
	static public void setDiv(double d)
	{
		diversite = d;
	}
	
	public Matrix fusion(Matrix mere)
    {
    	Matrix A = new Matrix(this);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
            {
            	//A.data[i][j] *= multiple;
            	if(Math.random() <= 0.4)
            	{
            		A.data[i][j] = mere.data[i][j];
            	}
            	if((int)(1000*Math.random()) == 42)
            	{
	            	double rand = Math.random()-0.5;
	            	if(rand >= 0)
	            	{
	            		rand *= diversite;
	            		rand *= rand;
	            	}
	            		
	            	else
	            	{
	            		rand *= diversite;
	            		rand *= - rand;
	            	}
	            	
	            	A.data[i][j] += rand;
	            	//System.out.println("Mutation of " + rand + " with Div = " + diversite);
            	}
            }
         
        A.saturation(-4, 4);
        return A;
    }
	    
    public void saturation(int min, int max)
    {
    	Matrix A = this;
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
            {
                if(A.data[i][j] < min)A.data[i][j]=min;
                else if(A.data[i][j] > max)A.data[i][j]=max;
            }
    }

	
	public static double tanh(double x)
	{
		return (Math.exp(2*x) - 1 )/ (Math.exp(2*x) + 1);
		//return (1 / (Math.exp(-2*x) + 1));
	}
	
	
    public Matrix masque(Matrix leMasque)
    {
    	Matrix A = this;
    	if (leMasque.M != A.M || leMasque.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
    	for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
            {
            	if(A.data[i][j] < leMasque.data[i][j])
            		A.data[i][j] = 0;	            	
            }
        return A;
    }
	
	
    public void masque(double absMasque)
    {
    	for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
            {
            	if(Math.abs(this.data[i][j]) < absMasque)
            	{
            		this.data[i][j] = 0;
            	}
            }
    }
    
    public double get(int j, int i)
    {
    	if(j < M && i < N && j >= 0 && i >= 0)
    		return data[j][i];
    	else
    		System.out.println("Erreur de dimensions, demande de get d'index [" + j + "][" + i + "] dans [" + (M-1) + "][" + (N-1) + "]"); return 0; 
    }
    
    public void set(int j, int i, double value)
    {
    	if(j < M && i < N && j >= 0 && i >= 0)
    		data[j][i] = value;
    	else
    		System.out.println("Erreur de dimensions, demande de set d'index [" + j + "][" + i + "] dans [" + (M-1) + "][" + (N-1) + "]"); 
    }
    
    public Matrix times(int multiple)
    {
    	Matrix A = this;
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                A.data[i][j] *= multiple;
        return A;
    }
    
    public Matrix timesTanH()
    {
    	Matrix A = this;
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                A.data[i][j] = tanh(A.data[i][j]);
        return A;
    }
    
    public void show(String label)
    {
    	System.out.println(label + " :");
    	show();
    }
    public Matrix minus(double B) {
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = this.data[i][j] - B;
        return C;
    }
    
    public String toString()
    {
    	String reponse = "";
    	reponse += N + " " + M + " ";
    	for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) 
                reponse += data[i][j] + " ";
        }
    	return reponse;
    }
    
    public void set(Matrix d)
    {
    	for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                this.data[i][j] = d.data[i][j];
    }


    /********************* END OF PERSONALISED SECTION *********************/

    // create M-by-N matrix of 0's
    public Matrix(int M, int N) {
        this.M = M;
        this.N = N;
        data = new double[M][N];
    }

    // create matrix based on 2d array
    public Matrix(double[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new double[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                    this.data[i][j] = data[i][j];
    }

    // copy constructor
    public Matrix(Matrix A) { this(A.data); }

    
    // create and return a random M-by-N matrix with values between -1 and 1
    public static Matrix random(int m, int n) {
        Matrix a = new Matrix(m, n);
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                    a.data[i][j] = Math.random() * 2 - 1;
        return a;
    }
    /*
    public static Matrix random(int M, int N) {
        Matrix A = new Matrix(M, N);        
        for (int i = 0; i < M; i++)
        {
            for (int j = 0; j < N; j++)
            {
            	double rand = 1.61*Math.random()-0.82;
            	if(rand >= 0)
            	{
            		rand *= rand;
            	}
            		
            	else
            	{
            		rand *= - rand;
            	}
            	rand *= 6;
            	
            	A.data[i][j] = rand;
            }
        }
        A.saturation(-4, 4);
        return A;
    }
    */

    // create and return the N-by-N identity matrix
    public static Matrix identity(int N) {
        Matrix I = new Matrix(N, N);
        for (int i = 0; i < N; i++)
            I.data[i][i] = 1;
        return I;
    }

    // swap rows i and j
    public void swap(int i, int j) {
        double[] temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    // create and return the transpose of the invoking matrix
    public Matrix transpose() {
        Matrix A = new Matrix(N, M);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                A.data[j][i] = this.data[i][j];
        return A;
    }

    // return C = A + B
    public Matrix plus(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j] + B.data[i][j];
        return C;
    }


    // return C = A - B
    public Matrix minus(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j] - B.data[i][j];
        return C;
    }

    // does A = B exactly?
    public boolean eq(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                if (A.data[i][j] != B.data[i][j]) return false;
        return true;
    }

    // return C = A * B
    public Matrix times(Matrix B) {
        Matrix A = this;
        if (A.N != B.M) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(A.M, B.N);
        for (int i = 0; i < C.M; i++)
            for (int j = 0; j < C.N; j++)
                for (int k = 0; k < A.N; k++)
                    C.data[i][j] += (A.data[i][k] * B.data[k][j]);
        return C;
    }


    // return x = A^-1 b, assuming A is square and has full rank
    public Matrix solve(Matrix rhs) {
        if (M != N || rhs.M != N || rhs.N != 1)
            throw new RuntimeException("Illegal matrix dimensions.");

        // create copies of the data
        Matrix A = new Matrix(this);
        Matrix b = new Matrix(rhs);

        // Gaussian elimination with partial pivoting
        for (int i = 0; i < N; i++) {

            // find pivot row and swap
            int max = i;
            for (int j = i + 1; j < N; j++)
                if (Math.abs(A.data[j][i]) > Math.abs(A.data[max][i]))
                    max = j;
            A.swap(i, max);
            b.swap(i, max);

            // singular
            if (A.data[i][i] == 0.0) throw new RuntimeException("Matrix is singular.");

            // pivot within b
            for (int j = i + 1; j < N; j++)
                b.data[j][0] -= b.data[i][0] * A.data[j][i] / A.data[i][i];

            // pivot within A
            for (int j = i + 1; j < N; j++) {
                double m = A.data[j][i] / A.data[i][i];
                for (int k = i+1; k < N; k++) {
                    A.data[j][k] -= A.data[i][k] * m;
                }
                A.data[j][i] = 0.0;
            }
        }

        // back substitution
        Matrix x = new Matrix(N, 1);
        for (int j = N - 1; j >= 0; j--) {
            double t = 0.0;
            for (int k = j + 1; k < N; k++)
                t += A.data[j][k] * x.data[k][0];
            x.data[j][0] = (b.data[j][0] - t) / A.data[j][j];
        }
        return x;
   
    }

    // print matrix to standard output
    public void show() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) 
                System.out.printf("%9.4f ", data[i][j]);
            System.out.println();
        }

        System.out.println();
    }
}
