local customerId = KEYS[1];

local function getAccountBalance(accountId)
    local balance = redis.call('GET', accountId);
    if (balance == nil) then
        return 0;
    else
        return balance;
    end
end

local total = 0;
if (ARGV[1] ~= nil) then
total = total + getAccountBalance(ARGV[1] .. ':' .. customerId)
end

if (ARGV[2] ~= nil) then
total = total + getAccountBalance(ARGV[2] .. ':' .. customerId)
end

if (ARGV[3] ~= nil) then
total = total +  getAccountBalance(ARGV[3] .. ':' .. customerId)
end

return total;