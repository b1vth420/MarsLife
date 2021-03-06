package me.b1vth420.marsApi;

import me.b1vth420.marsApi.Data.Config;
import me.b1vth420.marsApi.Data.FileManager;
import me.b1vth420.marsApi.Data.MySQL.MySQL;
import me.b1vth420.marsApi.Data.MySQL.SQLManager;
import me.b1vth420.marsApi.Utils.SignMenuFactory;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Api extends JavaPlugin {

    private static Api inst;
    private SQLManager sql;
    private SignMenuFactory signMenuFactory;
    private static Economy econ;


    public Api() {
        inst = this;
    }

    @Override
    public void onEnable() {
        init();
    }

    @Override
    public void onDisable() {
        sql.onDisable();
        MySQL.saveUsers();
    }

    private void init() {
        FileManager.check();
        Config.getInst().load();
        registerDatabase();
        setupEconomy();
        getSQLManager().createTable("CREATE TABLE IF NOT EXISTS marsUsers(UUID varchar(36) not null, name VARCHAR(16) not null, bankMoney text not null, isCredit text not null, creditSize text not null, creditTime text not null, diseases text not null, primary key(uuid))");
        if (this.signMenuFactory == null) this.signMenuFactory = new SignMenuFactory(this);
        MySQL.loadUsers();
    }

    public static Api getInst() {
        if (inst == null) return new Api();
        return inst;
    }

    private void registerDatabase() {
        sql = new SQLManager(this);
    }

    public SQLManager getSQLManager() {
        return this.sql;
    }

    public SignMenuFactory getSignMenuFactory() {
        return this.signMenuFactory;
    }

    public static Economy getEconomy() {
        return econ;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
