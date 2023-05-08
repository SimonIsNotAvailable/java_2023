import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {
    /**
     * Список запросов, заполняемых из файла input.txt
     */
    private static List<String> dataList;
    /**
     * Число кэшируемых запросов, заполняемых из файла input.txt
     */
    private static int maxCachedRequests;
    /**
     * Число запросов в файле, заполняемых из файла input.txt
     */
    private static int requestQuantity;
    /**
     * Счетчик запросов к распределенной системе
     */
    private static int distributedRequestQuantity;
    /**
     * Массив для кэшируемых запросов
     */
    private static int[] cache;

    public static void main(String[] args) throws IOException {

        readResources();
        cache = new int[maxCachedRequests];
        processRequests(cache);
        writeResult();
    }

    /**
     * Метод осуществляет запись в значения поля distributedRequestQuantity в файл output.txt
     */

    private static void writeResult() throws IOException {
        PrintWriter writer = new PrintWriter("output.txt", StandardCharsets.UTF_8);
        writer.println(distributedRequestQuantity);
        writer.close();
    }

    /**
     * Метод осуществляет обработку запросов
     */

    private static void processRequests(int[] cache) {

        for (int i = 1; i < dataList.size(); i++) {

            int requestNumber = Integer.parseInt(dataList.get(i));
            int index = isCached(requestNumber);                    //проверка не закеширован ли запрос

            if (index == -1) {
                //закэшировать
                cacheRequest(requestNumber, i);
            } else {
                getFromCache(index);
            }
        }
    }

    /**
     * Метод осуществляет работу с кэшом
     */
    private static void cacheRequest(int requestNumber, int dataListIndex) {

        for (int j = 0; j < cache.length; j++) {
            if (cache[j] == 0 && isCached(requestNumber) == -1) {
                distributedRequest();
                cache[j] = requestNumber;
                break;
            } else if (cache[cache.length - 1] != 0) { // кэш заполнен
                List<Integer> shortList = dataList.subList(dataListIndex, memorySavingNumber(dataListIndex))
                        .stream()
                        .mapToInt(Integer::parseInt)
                        .boxed()
                        .toList();
                for (int k = 0; k < cache.length; k++) {
                    if (!shortList.contains(cache[k])) {
                        cache[k] = 0;
                    }
                }
            }
        }
    }
    
    private static int memorySavingNumber(int dataListIndex) {
        return Math.min(dataListIndex + dataList.size()/2 , dataList.size());
    }

    /**
     * Метод осуществляет поиск по массиву Кэша
     * @return индекс найденного элемента или -1 в случае если значение не найдено
     */
    private static int isCached(int requestNumber) {
        return search(cache, 0, cache.length - 1, requestNumber);
    }

    /**
     * Метод осуществляет рекурсивный бинарный поиск по массиву
     * @return индекс найденного элемента или -1 в случае если значение не найдено
     */
    public static int search(int[] arr, int firstElement, int lastElement, int elementToSearch) {

        if (lastElement >= firstElement) {
            int mid = firstElement + (lastElement - firstElement) / 2;

            if (arr[mid] == elementToSearch)
                return mid;

            if (arr[mid] > elementToSearch)
                return search(arr, firstElement, mid - 1, elementToSearch);

            return search(arr, mid + 1, lastElement, elementToSearch);
        }

        return -1;
    }

    /**
     * Метод добавляет к количеству запросов к распределенной системе
     */
    private static void distributedRequest() {
        distributedRequestQuantity++;
    }

    /**
     * Записывает в поле dataList список из файла input.txt
     */
    private static void readResources() throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        try {
            String line = br.readLine();

            String[] array = line.split(" ");
            maxCachedRequests = Integer.parseInt(array[0]);
            requestQuantity = Integer.parseInt(array[1]);

            dataList = new ArrayList<>(requestQuantity);

            while (line != null) {
                dataList.add(line);
                line = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
    }

    // в контексте задания реализация не требуется
    private static void getFromCache(int index) {
        //TODO
    }
}