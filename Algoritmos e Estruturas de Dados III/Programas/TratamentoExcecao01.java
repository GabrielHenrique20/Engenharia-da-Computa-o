/**
 * Tratamento de excecao
 * @author Max do Val Machado
 * @version 2 01/2015
 */
public class TratamentoExcecao01 {
   public static int divisao(int a, int b) throws ArithmeticException, NumberFormatException{
       return a /b;
   } 
    public static void main(String[] args) {
      try {
         System.err.println(divisao(5,2));

      } catch (ArithmeticException e){
         System.out.println("Nao tem divisao por zero!");

      } 
      catch(NumberFormatException e){
         System.out.println("Erro de tipo");
      }finally {
         System.out.println("FIM DE PROGRAMA!!!");
      }
   }
 }