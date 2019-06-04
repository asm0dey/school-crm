# CRM для школ [![Dependabot Status](https://api.dependabot.com/badges/status?host=github&repo=asm0dey/school-crm)](https://dependabot.com)

## Цель

Этот проект предназначен для школ, которые испытывают потребность в том, чтобы коммуницировать с родителями своих учеников.

## Возможности

1. Открытая регистрация учеников
1. Перевод учеников между классами
1. Автоматический сдвиг классов (происходит ночью 31 августа)
1. Автоматическое удаление классов, которые стали неактуальны после сдвига (вместе с учениеками)
1. Интеграция с Mailgun (именно Mailgun потому что в нём есть квота бесплатных писем, может быть актуально для школ, у которых нет на это бюджета (видимо, всех государственных)
1. Ролевая модель:
    1. Администратор может     
        1. создавать и удалять пользователей, 
        1. редактировать их профили, 
        1. переводить учеников между классами и удалять их, 
        1. создавать новые классы и группы, 
    1. Редактор может составлять и отправлять письма

## TBD

1. [x] Создание групп 
1. [ ] Добавление и удаление туда учеников

## Настройка

### Настройка приложения

Базовая настройка осуществляется в файле `conf/application.conf`.

```
db {
    url="jdbc:postgresql://localhost/crm" #JDBC url
    user="crm"
    password="crm"
}
application {
    secret="ZJPmn1iw0a7RQ8hot45KlWF/DHs3rK42m/wG0iw1bFIlzTkmTw0KRo26Lceh" # Ключ, с которым шифруется Cookie приложения
    securePort = 8443 #Порт, на котором поднимается https https версия приложения
    dateFormat = "dd/MM/yyyy" #Формат даты
}
server.http2.enabled = true
crm {
    allowedDegrees = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11] # Разрешённые номера классов
    allowedLetter  = ["А", "Б", "В", "Г", "Д"] # Разрешённые буквы классов
}
mailgun {
    key = "secret-key" # Секретный ключ, с которым мы обращаемся в mailgun
}
flash.cookie {
    name = "flash"
}
```

## Первый запуск
```
docker-compose -f docker-compose/docker-compose.yml up -d
mvn jooby:run
```

### Настройка логгирования

Логгирование настраивается в `conf/logback.xml` так, как описано [тут](https://logback.qos.ch/manual/configuration.html)

## Благодарности 

Большое спасибо команде Excelsior JET за предоставленную лицензию на Excelsior JET Windows и Linux!

[<img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALQAAAA8CAMAAAD48GC1AAAABGdBTUEAAK/INwWK6QAAABl0RVh0U29mdHdhcmUAQWRvYmUgSW1hZ2VSZWFkeXHJZTwAAAMAUExURS4uLnR0dFZ10RZIddfb6VxcXJi3zK2trVJqpHJycpycnGN9zoWFhTpKl/v7/IOPtExpw5W2+D9Qms7OzuDg4GqZ+HBwcElkuZqkyjY8XDxNmGmE1XmIunx8fPPz82xsbJ260SlXhKW2yWaIqE5lonej+x8zmdrl+EJWmwAAAFNyzbnL6bO619vg63iIqkZbnkBAQNra2uzx92d6s0xMTGyCx1p0qfL0+HSMwpqq1WlpaT09PWd2qoat/RIsVlFtx+rt9FtyuImu/I2kuuXq8jlUuT4+PkVrktXV1TtBTczS5ThJpERScnibtUd2m1Vrl8XX91NTU12DpDxWpTpqlLS0tDg4OEVetaC70VVruTc8Shk4ZEdZgm+CvcHBwcLM5TVjjDo6OmFhYY6x+isrK4SoxElfoGd6nKampszU38HI2kthhbzD3jtprSxJdDBbh2ZmZlJniICp/l6P6KzG+SI9blJ3mzpFZiNSf6jC1rK1vaqzz9/l7uvr687b5dLe9I+Pj1NkoW5ubnqPyztIdVlsoihAqpSfx73L20VFRbPB07fO+UFcrbvF06Wv0X6hvIqVwY6aw1p1y1x5zjRNs6G+2GmWtHac17DD5Z6rxYCPv1N+or+/v5Gj0VJlq8jQ5uHq+szY7ubn6Ku60d/o8oSVy4yczVxyw7LF2D1Og7e9yYes9qa02ktioYic1a6xvMbV4lFrsHB/s1xuq6u82oas+oSp9Z2t0lt4qy1giYKdzUt7yXCL0YGp+EBRrff5+0RcozVMkipBhUt+n15xokdalIacxR9EbTtXfDE0Pi4xObW5v0NgwkxhlC0uMS1clxoaGrvQ+rLI2lFhnEpeqXKQrB1Oez9SpOju+Cw/noOs/YWr+kFTjjdQnDI0NycnKThHlmltf1Zwpldmp0paoGyRyF1xk569+DFSolGC1jE2Roqr74iPo7/BykBKXZCvxTg7QT5avTNFozY4QEZYqLvP5lJ0viosMF98vUFtm1N3qjExMTQ0NDY2Nv///////w4ZmGwAAAEAdFJOU////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wBT9wclAAAMtklEQVR42tTaC1wTRx4HcCJgauAiEJAoEF6KBBMlAW4xIgSNr3AqIA8BBcFHg2IJ2lZ6gMZaq6BVMNQqVFBAsa32lNSqLYqvauvRq/UUH5V6Vz312l4t2lo8yb/3n90kEEBFfB2/D59lZnY2+83s7GbIB4vfe2Asfqf6jdX3nIywriBo96jXWT0n/aKiCHpEP1aPSqojovU9y8zyTyboPj0rbdAjHiH/D+h+mBFD2+THohWFrtPNU1MTEBJy7Ni1yVlZWSP6Pc+Y0KlDh772NmYqScHiwavsZ5oyqk0++GDPO95ZWanPH+3u7h7XavY4nSuNvG5GbZuRH6M6zr2zgKazIokbuLWpSWLJNlbi3r20Qb9tQHucVlBLro8aSYB79uwxSA1hiu90RJur3B+2m9QfEy2TyfRDGbOHx2u51ODryMN5YW8IzpC26m9GLieHyGRujgAqDQA4siQAbAm4SSSQxwOBGxbzsJ1tmwcCjQx3gptMZgssVp7MDdigIt3Z2EMl6zRqdRrGGhPFJNCYfEycEd3CoD08PBZLB18fNWqm/cuRg+VSeYVCfn5w5Mv2M0d9s2ePAb7n4vIW+rV5jjIVcKCPWx4bhw45buMdZY7JMqYqY/OwJGPnsR37aEhd5qjigIa0qsh+dl4ftsAgNPAMKv8Hh0Gr1ep7Q6e+7THVo7fHr9T5l3GMIzfXVYnllIJ/fO+GLZvrNn8984MDBy4ax3v5PTVJnkrtBipQq5OTUaEmaLaa/iFV27w+aiDBFlJXq9m8ZIjlcfA4FvSxVvGi3CDwocQHo3GUPXpvV1CR9vZfb5Zu2bEphQtcfkBzUNCy4SlVVUN++PjARUOOMWgeT80WcEDtJkClbTt0vzxbfFts7Jbs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">](https://www.excelsiorjet.com/)
