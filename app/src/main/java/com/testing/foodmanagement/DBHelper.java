package com.testing.foodmanagement;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Canteen.db";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 17);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE Users(" +
                "firstName TEXT, " +
                "lastName TEXT, " +
                "email TEXT PRIMARY KEY, " +
                "password TEXT, " +
                "phoneNo TEXT, " +
                "address TEXT, " +
                "profile_image BLOB)");

        MyDB.execSQL("CREATE TABLE Orders (" +
                "orderId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT, " +
                "itemName TEXT, " +
                "itemPrice TEXT, " +
                "itemQuantity TEXT, " +
                "branch TEXT, " +
                "phone TEXT, " +
                "paymentMethod TEXT, " + // New column for payment method
                "customerLocation TEXT,"+ // New column for customer location
                "FOREIGN KEY(email) REFERENCES Users(email))");

        MyDB.execSQL("CREATE TABLE finalOrder(" +
                "cur_date DATE DEFAULT CURRENT_DATE, " +
                "cur_time TIME DEFAULT CURRENT_TIME, " +
                "email TEXT, " +
                "orderName TEXT, " +
                "orderQuantity TEXT, " +
                "orderPrice TEXT)");

        MyDB.execSQL("CREATE TABLE food_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "category TEXT, " +
                "description TEXT, " +
                "price REAL, " +
                "available INTEGER, " +
                "image BLOB)");

        MyDB.execSQL("CREATE TABLE Branches(" +
                "branchName TEXT PRIMARY KEY, " +
                "phone TEXT, " +
                "email TEXT, " +
                "openHours TEXT, " +
                "location TEXT)"); // Added the address column



        MyDB.execSQL("CREATE TABLE Categories(" +
                "categoryId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "categoryName TEXT, " +
                "categoryImage BLOB)");

        MyDB.execSQL("CREATE TABLE Customizations(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "price REAL)");

        MyDB.execSQL("CREATE TABLE FoodCustomizations(" +
                "foodItemId INTEGER, " +
                "customizationId INTEGER, " +
                "FOREIGN KEY(foodItemId) REFERENCES food_items(id), " +
                "FOREIGN KEY(customizationId) REFERENCES Customizations(id))");

        MyDB.execSQL("CREATE TABLE Cart(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "food_id INTEGER, " +  // Ensure this column is included
                "name TEXT, " +
                "price REAL," +
                "quantity INTEGER," +
                "image BLOB," +
                "FOREIGN KEY(food_id) REFERENCES food_items(id))"); // Correct the foreign key reference



        MyDB.execSQL("CREATE TABLE Promotions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "promotion_name TEXT, " +
                "promotion_code TEXT, " +
                "description TEXT, " +
                "promotion_start_date TEXT, " +
                "promotion_end_date TEXT, " +
                "promotion_discount REAL, " +
                "image BLOB)");



        Log.d("DBHelper", "Database tables created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("DROP TABLE IF EXISTS Users");
        MyDB.execSQL("DROP TABLE IF EXISTS Orders");
        MyDB.execSQL("DROP TABLE IF EXISTS finalOrder");
        MyDB.execSQL("DROP TABLE IF EXISTS food_items");
        MyDB.execSQL("DROP TABLE IF EXISTS Branches");
        MyDB.execSQL("DROP TABLE IF EXISTS Categories");
        MyDB.execSQL("DROP TABLE IF EXISTS Customizations");
        MyDB.execSQL("DROP TABLE IF EXISTS FoodCustomizations");
        MyDB.execSQL("DROP TABLE IF EXISTS Cart");
        MyDB.execSQL("DROP TABLE IF EXISTS Promotions");
        onCreate(MyDB);
    }

    public long addPromotion(String promotionName, String promotionCode, String description,
                             String promotionStartDate, String promotionEndDate,
                             double promotionDiscount, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("promotion_name", promotionName);
        values.put("promotion_code", promotionCode);
        values.put("description", description);
        values.put("promotion_start_date", promotionStartDate);
        values.put("promotion_end_date", promotionEndDate);
        values.put("promotion_discount", promotionDiscount);
        values.put("image", image);

        long result = db.insert("Promotions", null, values);
        db.close();
        return result;
    }

    public List<Promotion> getAllPromotions() {
        List<Promotion> promotionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Adjust the query to select all necessary columns
        Cursor cursor = db.rawQuery("SELECT * FROM Promotions", null);

        if (cursor.moveToFirst()) {
            do {
                // Retrieve all columns from the cursor
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String promotionName = cursor.getString(cursor.getColumnIndex("promotion_name"));
                @SuppressLint("Range") String promotionCode = cursor.getString(cursor.getColumnIndex("promotion_code"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") String promotionStartDate = cursor.getString(cursor.getColumnIndex("promotion_start_date"));
                @SuppressLint("Range") String promotionEndDate = cursor.getString(cursor.getColumnIndex("promotion_end_date"));
                @SuppressLint("Range") double promotionDiscount = cursor.getDouble(cursor.getColumnIndex("promotion_discount"));
                @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));

                // Create a Promotion object with all attributes
                Promotion promotion = new Promotion(id, promotionName, promotionCode, description,
                        promotionStartDate, promotionEndDate, promotionDiscount, image);
                promotionList.add(promotion);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return promotionList;
    }


    public void addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("categoryName", category.getCategoryName());
        values.put("categoryImage", category.getCategoryImage()); // Store image as byte array

        long result = db.insert("Categories", null, values);
        db.close();
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Categories", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int categoryId = cursor.getInt(cursor.getColumnIndex("categoryId"));
                @SuppressLint("Range") String categoryName = cursor.getString(cursor.getColumnIndex("categoryName"));
                @SuppressLint("Range") byte[] categoryImage = cursor.getBlob(cursor.getColumnIndex("categoryImage"));

                Category category = new Category(categoryId, categoryName, categoryImage);
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categoryList;
    }



    public int addFoodItem(FoodItem foodItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", foodItem.getName());
        values.put("category", foodItem.getCategory());
        values.put("description", foodItem.getDescription());
        values.put("price", foodItem.getPrice());
        values.put("available", foodItem.isAvailable() ? 1 : 0);
        values.put("image", foodItem.getImage());  // Store image as byte array

        int foodId = (int) db.insert("food_items", null, values);
        db.close();
        return foodId;
    }

    public List<FoodItem> getAllFoodItems() {
        List<FoodItem> foodItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM food_items", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                @SuppressLint("Range") boolean available = cursor.getInt(cursor.getColumnIndex("available")) > 0;
                @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));

                FoodItem item = new FoodItem(id, name, category, description, price, available, image);
                foodItemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodItemList;
    }

    public boolean insertData(String firstName, String lastName, String email, String password, String phoneNo, String address, byte[] profileImage) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("phoneNo", phoneNo);
        contentValues.put("address", address);
        contentValues.put("profile_image", profileImage);
        long result = MyDB.insert("Users", null, contentValues);
        return result != -1;
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM Users WHERE email=?", new String[]{email});
        return cursor.getCount() > 0;
    }

    public boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM Users WHERE email=? AND password=?", new String[]{email, password});
        return cursor.getCount() > 0;
    }

    public byte[] getUserProfileImage(String email) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT profile_image FROM Users WHERE email=?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            byte[] image = cursor.getBlob(0);
            cursor.close();
            return image;
        }
        return null;
    }

    public Cursor getData(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.rawQuery("SELECT * FROM Users WHERE email=?", new String[]{email});
    }

    public boolean addBranch(Branch branch) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("branchName", branch.getBranchName());
        values.put("phone", branch.getPhone());
        values.put("email", branch.getEmail());
        values.put("openHours", branch.getOpenHours());
        values.put("location", branch.getLocation());

        long result = db.insert("Branches", null, values);
        db.close();
        return result != -1;
    }

    public List<Branch> getAllBranches() {
        List<Branch> branchList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Branches", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String branchName = cursor.getString(cursor.getColumnIndex("branchName"));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("phone"));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
                @SuppressLint("Range") String openHours = cursor.getString(cursor.getColumnIndex("openHours"));
                @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex("location"));

                Branch branch = new Branch(branchName, phone, email, openHours, location);
                branchList.add(branch);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return branchList;
    }

    public List<FoodItem> getLastFiveAddedFoodItems() {
        List<FoodItem> foodItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "food_items",
                null,
                null,
                null,
                null,
                null,
                "id DESC",
                "5"
        );
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                @SuppressLint("Range") boolean available = cursor.getInt(cursor.getColumnIndex("available")) > 0;
                @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));

                FoodItem item = new FoodItem(id, name, category, description, price, available, image);
                foodItemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodItemList;
    }

    public List<FoodItem> getFoodItemsByCategory(String categoryName) {
        List<FoodItem> foodItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM food_items WHERE category = ?", new String[]{categoryName});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                @SuppressLint("Range") boolean available = cursor.getInt(cursor.getColumnIndex("available")) > 0;
                @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));

                FoodItem item = new FoodItem(id, name, category, description, price, available, image);
                foodItemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodItemList;
    }

    public List<FoodItem> getAvailableFoodItems() {
        List<FoodItem> foodItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM food_items WHERE available = 1", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                @SuppressLint("Range") boolean available = cursor.getInt(cursor.getColumnIndex("available")) > 0;
                @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));

                FoodItem item = new FoodItem(id, name, category, description, price, available, image);
                foodItemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodItemList;
    }
    public List<FoodItem> getFoodItemsByName(String itemName) {
        List<FoodItem> foodItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM food_items WHERE name = ?", new String[]{itemName});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                @SuppressLint("Range") boolean available = cursor.getInt(cursor.getColumnIndex("available")) > 0;
                @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));

                FoodItem item = new FoodItem(id, name, category, description, price, available, image);
                foodItemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodItemList;
    }

    public long addCustomization(Customization customization) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", customization.getName());
        values.put("price", customization.getPrice());

        long customizationId = db.insert("Customizations", null, values);
        db.close();
        return customizationId;
    }
    public List<Customization> getAllCustomizations() {
        List<Customization> customizations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Customizations", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));

                Customization customization = new Customization(id, name, price);
                customizations.add(customization);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return customizations;
    }
    public void assignCustomizationsToFoodItem(int foodItemId, List<Integer> customizationIds) {
        Log.d("DBHelper", "assignCustomizationsToFoodItem called with foodItemId: " + foodItemId + " and customizationIds: " + customizationIds);
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int customizationId : customizationIds) {
                ContentValues values = new ContentValues();
                values.put("foodItemId", foodItemId);
                values.put("customizationId", customizationId);
                long result = db.insert("FoodCustomizations", null, values);
                if (result == -1) {
                    Log.e("DBHelper", "Failed to insert customizationId " + customizationId + " for foodItemId " + foodItemId);
                } else {
                    Log.d("DBHelper", "Successfully inserted customizationId " + customizationId + " for foodItemId " + foodItemId);
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
    }
    public List<Customization> getCustomizationsForFoodItem(int foodItemId) {
        List<Customization> customizationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c.id, c.name, c.price " +
                "FROM Customizations c " +
                "JOIN FoodCustomizations fc ON c.id = fc.customizationId " +
                "WHERE fc.foodItemId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(foodItemId)});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));

                Customization customization = new Customization(id, name, price);
                customizationList.add(customization);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return customizationList;
    }
    // Add the method to get a food item by its name
    public FoodItem getFoodItemById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        FoodItem foodItem = null;

        try {
            cursor = db.rawQuery("SELECT * FROM food_items WHERE id = ?", new String[]{String.valueOf(id)});
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") int itemId = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                @SuppressLint("Range") boolean available = cursor.getInt(cursor.getColumnIndex("available")) > 0;
                @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));

                foodItem = new FoodItem(itemId, name, category, description, price, available, image);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return foodItem;
    }

    public void addToCart(CartItem cartItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("food_id", cartItem.getId()); // Add food_id to the Cart table
        values.put("name", cartItem.getName());
        values.put("price", cartItem.getPrice());
        values.put("quantity", cartItem.getQuantity());
        values.put("image", cartItem.getImage());
        long result = db.insert("Cart", null, values);
        db.close();
        if (result == -1) {
            // Handle the error if the insertion fails
            System.out.println("Failed to add to cart.");
        } else {
            System.out.println("Added to cart successfully.");
        }
    }

    public List<CartItem> getAllCartItems() {
        List<CartItem> cartItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Cart", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));

                // Assuming you don't have any customizations stored in the database,
                // you can pass an empty list for now.
                List<Customization> customizations = new ArrayList<>(); // Or fetch customizations from the database

                // Create the CartItem with the list of customizations
                CartItem cartItem = new CartItem(id, name, price, quantity, image, customizations);
                cartItemList.add(cartItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cartItemList;
    }

    public void updateCartItem(CartItem cartItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", cartItem.getName());
        values.put("price", cartItem.getPrice());
        values.put("quantity", cartItem.getQuantity());
        values.put("image", cartItem.getImage());

        db.update("Cart", values, "id" + " = ?", new String[]{String.valueOf(cartItem.getId())});
        db.close();
    }


    public void deleteCartItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Cart", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public boolean updateProfilePicture(String email, Bitmap profileImage) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Convert Bitmap to byte array for storage
        if (profileImage != null) {
            contentValues.put("profile_image", getBytesFromBitmap(profileImage));
        }

        int result = MyDB.update("Users", contentValues, "email=?", new String[]{email});
        return result > 0; // Return true if update is successful
    }

    public Bitmap getProfileImage(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Bitmap profileImage = null;

        Cursor cursor = db.rawQuery("SELECT profile_image FROM Users WHERE email = ?", new String[]{email});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                byte[] imageBytes = cursor.getBlob(0);
                if (imageBytes != null) {
                    profileImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                }
            }
            cursor.close();
        }
        return profileImage;
    }


    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public String getFirstNameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String firstName = "";
        Cursor cursor = db.rawQuery("SELECT firstName FROM Users WHERE email = ?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            firstName = cursor.getString(0);
            cursor.close();
        }
        return firstName;
    }

    public String getLastNameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String lastName = "";
        Cursor cursor = db.rawQuery("SELECT lastName FROM Users WHERE email = ?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            lastName = cursor.getString(0);
            cursor.close();
        }
        return lastName;
    }

    public User getUserDetailsByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        String query = "SELECT firstName, lastName, email, phoneNo, address, profile_image FROM Users WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex("firstName"));
            @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex("lastName"));
            @SuppressLint("Range") String emailField = cursor.getString(cursor.getColumnIndex("email"));
            @SuppressLint("Range") String phoneNo = cursor.getString(cursor.getColumnIndex("phoneNo"));
            @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));
            @SuppressLint("Range") byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("profile_image"));
            Bitmap profileImage = imageBytes != null ? BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length) : null;

            user = new User(firstName, lastName, emailField, phoneNo, address, profileImage);
        }
        cursor.close();
        db.close();
        return user;
    }
    public Promotion getPromotionByName(String promotionName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Promotions", null, "promotion_name=?", new String[]{promotionName}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Promotion promotion = new Promotion(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("promotion_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getString(cursor.getColumnIndexOrThrow("promotion_start_date")),
                    cursor.getString(cursor.getColumnIndexOrThrow("promotion_end_date")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("promotion_discount")),
                    cursor.getBlob(cursor.getColumnIndexOrThrow("image"))
            );
            cursor.close();
            return promotion;
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }
    public List<CartItem> getCartItemsByCartId(int cartId) {
        List<CartItem> cartItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM Cart WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(cartId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));

                CartItem cartItem = new CartItem(id, name, price, quantity, image);
                cartItemList.add(cartItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return cartItemList;
    }

    public List<String> getAllUserEmails() {
        List<String> emails = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT email FROM users", null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        emails.add(cursor.getString(0));
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.e("DBHelper", "Error fetching emails", e);
            } finally {
                cursor.close();
            }
        }
        return emails;
    }

    public void deleteCustomization(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("customizations", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateCustomization(Customization customization) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", customization.getName());
        values.put("price", customization.getPrice());
        db.update("customizations", values, "id = ?", new String[]{String.valueOf(customization.getId())});
        db.close();
    }
    public List<CartItem> getCartItemByCartId(int cartId) {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Cart WHERE id = ?", new String[]{String.valueOf(cartId)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") int foodId = cursor.getInt(cursor.getColumnIndex("food_id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));

                CartItem item = new CartItem(id, name, price, quantity);
                cartItems.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cartItems;
    }
    @SuppressLint("Range")
    public List<String> getAllBranchNames() {
        List<String> branchNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT branchName FROM Branches", null);

        if (cursor.moveToFirst()) {
            do {
                branchNames.add(cursor.getString(cursor.getColumnIndex("branchName")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return branchNames;
    }
    public String getCustomerLocation(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Users",
                new String[]{"address"},
                "email = ?",
                new String[]{email},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));
            cursor.close();
            return address;
        }
        return null;
    }
    public String getCustomerPhone(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Users",
                new String[]{"phoneNo"},
                "email = ?",
                new String[]{email},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String phoneNo = cursor.getString(cursor.getColumnIndex("phoneNo"));
            cursor.close();
            return phoneNo;
        }
        return null;
    }

    public void addOrder(String email, String itemName, String itemPrice,
                         String itemQuantity, String branch, String phone,
                         String paymentMethod, String customerLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("itemName", itemName);
        values.put("itemPrice", itemPrice);
        values.put("itemQuantity", itemQuantity);
        values.put("branch", branch);
        values.put("phone", phone);
        values.put("paymentMethod", paymentMethod);
        values.put("customerLocation", customerLocation);

        db.insert("Orders", null, values);
        db.close();
    }
    public ArrayList<Order> getAllOrders() {
        ArrayList<Order> ordersList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT itemName, itemPrice, itemQuantity, branch, phone, customerLocation FROM Orders", null);

        if (cursor.moveToFirst()) {
            do {
                ordersList.add(new Order(
                        cursor.getString(0),  // itemName
                        cursor.getString(1),  // itemPrice
                        cursor.getString(2),  // itemQuantity
                        cursor.getString(3),  // branch
                        cursor.getString(4),  // phone
                        cursor.getString(5)   // customerLocation
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ordersList;
    }

    public ArrayList<Order> getOrdersByBranch(String branch) {
        ArrayList<Order> ordersList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT itemName, itemPrice, itemQuantity, branch, phone, customerLocation FROM Orders WHERE branch = ?", new String[]{branch});

        if (cursor.moveToFirst()) {
            do {
                ordersList.add(new Order(
                        cursor.getString(0),  // itemName
                        cursor.getString(1),  // itemPrice
                        cursor.getString(2),  // itemQuantity
                        cursor.getString(3),  // branch
                        cursor.getString(4),  // phone
                        cursor.getString(5)   // customerLocation
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ordersList;
    }


    public String getOrderDetails(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Orders", null, "orderId=?", new String[]{String.valueOf(orderId)}, null, null, null);

        StringBuilder orderDetails = new StringBuilder();
        if (cursor != null && cursor.moveToFirst()) {
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow("itemName"));
            String itemPrice = cursor.getString(cursor.getColumnIndexOrThrow("itemPrice"));
            String itemQuantity = cursor.getString(cursor.getColumnIndexOrThrow("itemQuantity"));
            String branch = cursor.getString(cursor.getColumnIndexOrThrow("branch"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String paymentMethod = cursor.getString(cursor.getColumnIndexOrThrow("paymentMethod"));
            String customerLocation = cursor.getString(cursor.getColumnIndexOrThrow("customerLocation"));

            orderDetails.append("Order ID: ").append(orderId).append("\n")
                    .append("Email: ").append(email).append("\n")
                    .append("Item: ").append(itemName).append("\n")
                    .append("Price: ").append(itemPrice).append("\n")
                    .append("Quantity: ").append(itemQuantity).append("\n")
                    .append("Branch: ").append(branch).append("\n")
                    .append("Phone: ").append(phone).append("\n")
                    .append("Payment Method: ").append(paymentMethod).append("\n")
                    .append("Location: ").append(customerLocation).append("\n");
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return orderDetails.toString();
    }
    public void cancelOrder(int orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Orders", "orderId=?", new String[]{String.valueOf(orderId)});
        db.close();
    }

    public int getNextOrderId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(orderId) FROM Orders", null);

        int nextOrderId = 1; // Default to 1 if no orders exist
        if (cursor.moveToFirst()) {
            nextOrderId = cursor.getInt(0) + 1;
        }
        cursor.close();
        db.close();

        return nextOrderId;
    }

    public void clearCart(int Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Cart", "Id = ?", new String[]{String.valueOf(Id)});
        db.close();
    }

    @SuppressLint("Range")
    public String getUserEmail(String phoneNumber) {
        String email = null;
        SQLiteDatabase db = this.getReadableDatabase(); // Get readable database instance
        Cursor cursor = db.rawQuery("SELECT email FROM Users WHERE phoneNo = ?", new String[]{phoneNumber});
        if (cursor != null && cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndex("email"));
        }
        if (cursor != null) {
            cursor.close();
        }
        return email;
    }



}
